package me.coley.bmf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.coley.bmf.attribute.clazz.InnerClass;
import me.coley.bmf.attribute.method.LocalVariableType;
import me.coley.bmf.consts.*;
import me.coley.bmf.consts.mapping.ConstMemberDesc;
import me.coley.bmf.consts.mapping.ConstName;
import me.coley.bmf.consts.mapping.ConstSignature;
import me.coley.bmf.mapping.ClassMapping;
import me.coley.bmf.mapping.Mapping;
import me.coley.bmf.mapping.MemberMapping;
import me.coley.bmf.mapping.MethodMapping;
import me.coley.bmf.signature.Signature;
import me.coley.bmf.type.Type;
import me.coley.bmf.util.ConstUtil;
import me.coley.bmf.util.ImmutableBox;
import me.coley.bmf.util.StreamUtil;
import me.coley.bmf.util.io.JarUtil;

/**
 * Helper for loaded programs packaged in JAR files.
 */
public class JarReader {
    private final Mapping mapping;
    private final File file;
    private Map<String, ClassNode> classEntries;
    private Map<String, byte[]> fileEntries;

    /**
     * Creates a JarReader without reading from the file initially. This also
     * prevents mappings from being generated initially.
     *
     * @param file
     */
    public JarReader(File file) {
        this(file, false);
    }

    /**
     * Creates a JarReader that can read the file initially. No mappings are
     * generated. initially.
     *
     * @param file Read and create ClassNodes on init.
     * @param read
     */
    public JarReader(File file, boolean read) {
        this(file, read, false);
    }

    /**
     * Creates a JarReader that can read the file and creates mappings
     * initially.
     *
     * @param file
     * @param read        Read and create ClassNodes on init.
     * @param genMappings Also create mappings on init.
     */
    public JarReader(File file, boolean read, boolean genMappings) {
        if ((file == null) || !file.exists()) {
            throw new IllegalArgumentException("Invalid file given: " + file.getAbsolutePath());
        }
        this.file = file;
        this.mapping = new Mapping();
        if (read) {
            read();
            if (genMappings) {
                genMappings();
            }
        }
    }

    /**
     * Reads from the jar file.
     */
    public void read() {
        try {
            // Main classes
            Map<String, byte[]> classEntryBytes = JarUtil.readJarClasses(file);
            fileEntries = JarUtil.readJarNonClasses(file);
            classEntries = new HashMap<String, ClassNode>();
            for (String className : classEntryBytes.keySet()) {
                try {
                    ClassNode cn = ClassReader.getNode(classEntryBytes.get(className));
                    classEntries.put(className, cn);
                } catch (Exception e) {
                    System.err.println("Failed parsing node: " + className);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the mappings.
     */
    public void genMappings() {
        pass1MakeClasses();
        pass2LinkHierarchy();
        pass3FinishClasses();
        pass4PatchConsts();
    }

    private void pass1MakeClasses() {
        Map<String, ClassNode> inners = new HashMap<>();
        for (String nodeName : classEntries.keySet()) {
            ClassNode node = classEntries.get(nodeName);
            if (node.innerClasses == null) {
                continue;
            }
            for (InnerClass ic : node.innerClasses.classes) {
                String innerName = ConstUtil.getClassName(node, ic.innerClassIndex);
                if (!innerName.contains("$")) {
                    continue;
                }
                // IDK why this happens but:
                //@formatter:off
                //
                // Inside BytecodeMeta$FlagTest:
                // 
                //     org/benf/cfr/reader/bytecode/BytecodeMeta$CodeInfoFlag of class org/benf/cfr/reader/bytecode/BytecodeMeta <--- ????
                //     org/benf/cfr/reader/bytecode/BytecodeMeta$FlagTest of class org/benf/cfr/reader/bytecode/BytecodeMeta
                //     org/benf/cfr/reader/bytecode/BytecodeMeta$1 of class org/benf/cfr/reader/bytecode/BytecodeMeta  <--- ??? 
                //
                // Has inner class data of inner classes of it's parent
                // Wat.
                //
                //@formatter:on
                if (!inners.containsKey(innerName)) {
                    // Was just going to rely on inner class attribute alone,
                    // but above really confuses me.
                    String outerName = innerName;
                    int index = outerName.lastIndexOf("$");
                    if (index > 0) {
                        outerName = outerName.substring(0, index);
                    }
                    ClassNode outer = classEntries.get(outerName);
                    inners.put(innerName, outer);
                }
            }
        }
        // Has to be done in order to prevent inner classes from being
        // registered before outer classes.
        for (String nodeName : StreamUtil.listOfSortedJavaNames(classEntries.keySet())) {
            ClassNode node = classEntries.get(nodeName);
            // Create and add ClassMapping values from the loaded nodes.
            if (inners.containsKey(nodeName)) {
                mapping.addMapping(mapping.createMappingFromInnerNode(inners.get(nodeName), node));
            } else {
                mapping.addMapping(mapping.createMappingFromNode(node));
            }

        }
    }

    private void pass2LinkHierarchy() {
        for (String nodeName : classEntries.keySet()) {
            ClassNode node = classEntries.get(nodeName);
            // Mapping hierarchy. Useful for searching for members in
            // parent/interface classes later on.
            String className = node.getName();
            String superName = node.getSuperName();
            // Ensure the super class has mappings.
            // Attempt to load it if it does not.
            // It will only be not-loaded if it does not exist in the jar.
            // Try to make runtime-mappings, but if the class does not exist
            // at runtime, then make a immutable mapping class.
            if (!mapping.hasClass(superName)) {
                ClassMapping temp = mapping.createMappingFromRuntime(superName);
                if (temp != null) {
                    mapping.addMapping(temp);
                } else {
                    mapping.addMapping(mapping.createImmutableMapping(superName));
                }
            }
            ClassMapping classMapping = mapping.getMapping(className);
            ClassMapping superMapping = mapping.getMapping(superName);
            // Link parent-child hierarchy
            mapping.setParent(classMapping, superMapping);
            mapping.addChild(superMapping, classMapping);
            for (int i : node.interfaceIndices) {
                String interfaceName = ConstUtil.getClassName(node, i);
                // Ensure interface exists
                if (!mapping.hasClass(interfaceName)) {
                    ClassMapping temp = mapping.createMappingFromRuntime(interfaceName);
                    if (temp == null) {
                        temp = mapping.createImmutableMapping(interfaceName);
                    }
                    mapping.addMapping(temp);
                }
                ClassMapping im = mapping.getMapping(interfaceName);
                // Link parent(interface)-child hierarchy
                if (im != null) {
                    mapping.addInterface(classMapping, im);
                    mapping.addChild(im, classMapping);
                }
            }
        }
    }

    private void pass3FinishClasses() {
        for (String nodeName : classEntries.keySet()) {
            ClassNode node = classEntries.get(nodeName);
            ClassMapping classMapping = mapping.getMapping(nodeName);
            // Add the members to the MappedClass associated with the
            // classnode.
            // Fun fact: This is done in a separate step because if members
            // references classes not in the mapping it would die. Lazily
            // loading them would be too much work since in the end they all
            // need to be loaded anyways.
            mapping.createMemberMappings(classEntries, classMapping, node);
        }
    }

    private void pass4PatchConsts() {
        // TODO: Figure out why we fail to update
        // Program.println in method body, but the declaration name is updated
        // (In tests directory, Test.jar contains the class 'Program')
        for (String nodeName : classEntries.keySet()) {
            ClassNode node = classEntries.get(nodeName);
            // Update class signature
            String strClassSig = node.getSignature();
            if (strClassSig != null) {
                Signature sig = Signature.read(mapping, strClassSig);
                node.setConst(node.signature.signature, new ConstSignature(sig));
            }
            // Update constant pool
            Map<Integer, Boolean> visited = new HashMap<>();
            Map<Integer, ConstRedirection> redirs = new HashMap<>();
            for (int i = 1; i < node.constants.size(); i++) {
                visitCP(node, i, null, visited, redirs);
            }
            // Update members.
            ClassMapping cm = mapping.getMapping(nodeName);
            List<MemberNode> members = new ArrayList<>();
            members.addAll(node.fields);
            members.addAll(node.methods);
            for (MemberNode member : members) {
                // Update member signature
                String strSig = member.getSignature();
                if (strSig != null) {
                    Signature sig = Signature.read(mapping, strSig);
                    node.setConst(member.signature.signature, new ConstSignature(sig));
                }
                // Update method
                if (member instanceof MethodNode) {
                    MethodNode meth = ((MethodNode) member);
                    if (meth.code != null) {
                        // Update variable types
                        if (meth.code.variableTypes != null) {
                            List<LocalVariableType> types = meth.code.variableTypes.localTypes;
                            for (LocalVariableType type : types) {
                                String sig = ConstUtil.getUTF8(node, type.signature);
                                node.setConst(type.signature, new ConstSignature(Signature.read(mapping, sig)));
                            }
                        }
                    }
                }
                // Updating member names if they haven't been updated already.
                String name = member.getName();
                String desc = member.getDesc();
                MemberMapping mm = mapping.getMemberMapping(cm, name, desc);
                if (mm instanceof MethodMapping) {
                    mm = mapping.getMemberInstance(cm, mm);
                }
                ConstRedirection redirName = redirs.getOrDefault(member.name,
                        new ConstRedirection(Usage.MEMBER_NAME, member.name, nodeName, name, desc));
                ConstRedirection redirDesc = redirs.getOrDefault(member.desc,
                        new ConstRedirection(Usage.MEMBER_DESC, member.desc, nodeName, name, desc));
                redirs.put(member.name, redirName);
                redirs.put(member.desc, redirDesc);
                int nameRedirIndex = redirName.getIndexForMember(node, Usage.MEMBER_NAME, nodeName, name, desc);
                int descRedirIndex = redirDesc.getIndexForMember(node, Usage.MEMBER_DESC, nodeName, name, desc);
                // Updating the consts
                node.setConst(nameRedirIndex, new ConstName(mm.name));
                member.name = nameRedirIndex;
                node.setConst(descRedirIndex, new ConstMemberDesc(mm.desc));
                member.desc = descRedirIndex;
            }
        }
    }

    private void visitCP(ClassNode node, int i, String ownerContext, Map<Integer, Boolean> visited,
                         Map<Integer, ConstRedirection> redirs) {
        // Skip if constant pool entry is null.
        Constant<?> c = node.getConst(i);
        if (c == null) {
            return;
        }
        switch (c.type) {
            case CLASS: {
                ConstClass cc = (ConstClass) c;
                int utfIndex = cc.getValue();
                ConstRedirection redir = redirs.getOrDefault(utfIndex, new ConstRedirection(Usage.CLASS, utfIndex));
                redirs.put(utfIndex, redir);
                int utfRedir = redir.getIndexForValue(node, Usage.CLASS);
                // Ensure the UTF is uses the class mapping.
                // If index changed, add constant to end of constant-pool for
                // the new usage. Otherwise, override the existing one.
                ConstUTF8 constName;
                String name = ConstUtil.getUTF8(node, utfIndex);
                if (node.isArray()) {
                    constName = new ConstMemberDesc(Type.variable(mapping, name));
                } else if (mapping.hasClass(name)) {
                    constName = new ConstName(mapping.getClassName(name));
                } else {
                    constName = new ConstName(new ImmutableBox<>(name));
                }
                if (utfRedir != utfIndex) {
                    node.setConst(utfRedir, constName);
                    cc.setValue(utfRedir);
                } else if (!isMapping(node.getConst(utfIndex))) {
                    node.setConst(utfIndex, constName);
                }
                break;
            }
            case STRING: {
                ConstString cs = (ConstString) c;
                int utfIndex = cs.getValue();
                ConstRedirection redir = redirs.getOrDefault(utfIndex, new ConstRedirection(Usage.STRING, utfIndex));
                redirs.put(utfIndex, redir);
                int utfRedir = redir.getIndexForValue(node, Usage.STRING);
                // If index changed, add constant to end of constant-pool for
                // the new usage.
                if (utfRedir != utfIndex) {
                    String content = ConstUtil.getUTF8(node, utfIndex);
                    node.setConst(utfRedir, new ConstUTF8(content));
                    cs.setValue(utfRedir);
                }
                break;
            }
            case FIELD:
            case METHOD:
            case INTERFACE_METHOD:
                AbstractMemberConstant amc = (AbstractMemberConstant) c;
                visitCP(node, amc.getClassIndex(), null, visited, redirs);
                visitCP(node, amc.getNameTypeIndex(), ConstUtil.getClassName(node, amc.getClassIndex()), visited, redirs);
                break;
            case INVOKEDYNAMIC:
                ConstInvokeDynamic indy = (ConstInvokeDynamic) c;
                visitCP(node, indy.getNameTypeIndex(), node.getName(), visited, redirs);
                break;
            case METHOD_HANDLE:
                ConstMethodHandle mh = (ConstMethodHandle) c;
                visitCP(node, mh.getIndex(), null, visited, redirs);
                break;
            case METHOD_TYPE:
                ConstMethodType mt = (ConstMethodType) c;
                visitCP(node, mt.getValue(), null, visited, redirs);
                // TODO: Handle the rest properly
// @formatter:off
/*
  BootstrapMethods:
    0: #91 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
      Method arguments:
        #93 (Ljava/lang/Object;)V
        #96 invokestatic me/coley/Program$2.lambda$0:(Lme/coley/cli/Command;)V
        #97 (Lme/coley/cli/Command;)V
        
   commands.getCommands().stream()
           .sorted(sorter)
           .forEach(
               c -> printf("%16s:%5s\n", c.getName(), c.getHandle())
           );
 */
// System.out.println(ConstUtil.getClassName(node, node.classIndex) + " MT:" + node.getConst(mt.getValue()).getValue());
// @formatter:on
                Constant<?> arg = node.getConst(mt.getValue());
                if (arg instanceof ConstUTF8 && !isMapping(arg)) {
                    ConstUTF8 utf = (ConstUTF8) arg;
                    node.setConst(mt.getValue(), new ConstMemberDesc(Type.method(mapping, utf.getValue())));
                }
                break;
            case NAME_TYPE:
                // Name-types need to be visited whilst knowing the owner of the
                // member being referenced.
                // If it is not known, skip over it. Another Constant will reference
                // it with the proper owner information.
                if (ownerContext == null) {
                    break;
                }
                ConstNameType nt = (ConstNameType) c;
                int nameIndex = nt.getNameIndex();
                int descIndex = nt.getDescIndex();
                String name = ConstUtil.getUTF8(node, nameIndex);
                String desc = ConstUtil.getUTF8(node, descIndex);
                // Get or create redirs for the name and desc utfs
                ConstRedirection redirName = redirs.getOrDefault(nameIndex,
                        new ConstRedirection(Usage.MEMBER_NAME, nameIndex, ownerContext, name, desc));
                ConstRedirection redirDesc = redirs.getOrDefault(descIndex,
                        new ConstRedirection(Usage.MEMBER_DESC, descIndex, ownerContext, name, desc));
                redirs.put(nameIndex, redirName);
                redirs.put(descIndex, redirDesc);
                int nameRedirIndex = redirName.getIndexForMember(node, Usage.MEMBER_NAME, ownerContext, name, desc);
                int descRedirIndex = redirDesc.getIndexForMember(node, Usage.MEMBER_DESC, ownerContext, name, desc);
                // Create mapping constants for the name and descriptor.
                // If we mapped the owner, we should be able to get the member.
                // Sometimes this is not the case though, like if we extend a class
                // that we do not have and reference its members.
                ConstName boxName = null;
                ConstMemberDesc boxDesc = null;
                if (mapping.hasClass(ownerContext)) {
                    ClassMapping cm = mapping.getMapping(ownerContext);
                    MemberMapping mm = mapping.getMemberMapping(cm, name, desc);
                    if (mm != null) {
                        mm = mapping.getMemberInstance(cm, mm);
                        boxName = new ConstName(mm.name);
                        boxDesc = new ConstMemberDesc(mm.desc);
                    } else {
                        boxName = new ConstName(new ImmutableBox<>(name));
                        if (desc.startsWith("(")) {
                            boxDesc = new ConstMemberDesc(Type.method(mapping, desc));
                        } else {
                            boxDesc = new ConstMemberDesc(Type.variable(mapping, desc));
                        }
                    }
                } else {
                    boxName = new ConstName(new ImmutableBox<>(name));
                    if (desc.startsWith("(")) {
                        boxDesc = new ConstMemberDesc(Type.method(mapping, desc));
                    } else {
                        boxDesc = new ConstMemberDesc(Type.variable(mapping, desc));
                    }
                }
                // Update the utf constants and the name-type constant's indecies.
                node.setConst(nameRedirIndex, boxName);
                nt.setNameIndex(nameRedirIndex);
                node.setConst(descRedirIndex, boxDesc);
                nt.setDescIndex(descRedirIndex);
                break;
            case UTF8:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
                // No action needed
                break;
        }
        // Mark the CP index as visited
        visited.put(i, true);
    }

    private boolean isMapping(Constant<?> c) {
        return (c instanceof ConstName) || (c instanceof ConstMemberDesc) || (c instanceof ConstSignature);
    }

    /**
     * Saves the ClassNode objects to a given jar destination.
     *
     * @param fileOut
     */
    public void saveJarTo(File fileOut) {
        JarUtil.writeJar(file, fileOut, classEntries, fileEntries);
    }

    /**
     * Saves the Mapping objects to a given file destination.
     *
     * @param fileOut
     * @throws IOException
     */
    public void saveMappingsTo(File fileOut) throws IOException {
        saveMappingsTo(fileOut, false);
    }

    /**
     * Loads mappings from the given file.
     *
     * @param fileIn
     */
    public void loadMappingsFrom(File fileIn) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileIn))) {
            ClassMapping cm = null;
            String in = null;
            while ((in = br.readLine()) != null) {
                if (in.startsWith("CLASS ")) {
                    String[] args = in.split(" ");
                    String orig = args[1];
                    String rename = args.length > 2 ? args[2] : null;
                    cm = this.mapping.getMapping(this.classEntries.get(orig));
                    if (cm != null && rename != null) {
                        cm.name.setValue(rename);
                    }
                } else if (cm != null) {
                    String tr = in.trim();
                    String[] args = tr.split(" ");
                    if (tr.startsWith("FIELD ") || tr.startsWith("METHOD ")) {
                        String orig = args[1];
                        String rename = args[2];
                        String desc = args[3];
                        MemberMapping mm = cm.getMemberMapping(orig, desc);
                        if (mm != null) {
                            mm.name.setValue(rename);
                        }
                    }
                }
            }
        }
    }

    /**
     * Saves the Mapping objects to a given file destination.
     *
     * @param fileOut
     * @param exclude Exclude classes/members not renamed
     * @throws IOException
     */
    public void saveMappingsTo(File fileOut, boolean exclude) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
        for (String name : classEntries.keySet()) {
            ClassMapping cm = mapping.getMappings().get(name);
            boolean include = !cm.name.getValue().equals(cm.name.original);
            String c = "CLASS " + cm.name.original + " " + cm.name.getValue() + "\n";
            List<MemberMapping> s = new ArrayList<MemberMapping>(cm.getMembers());
            s.sort(new Comparator<MemberMapping>() {
                @Override
                public int compare(MemberMapping m1, MemberMapping m2) {
                    boolean b1 = m1 instanceof MethodMapping;
                    boolean b2 = m2 instanceof MethodMapping;
                    if (b1 != b2) {
                        if (b1) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    return m1.name.original.compareTo(m2.name.original);
                }

            });
            List<String> m = new ArrayList<String>(s.size());
            for (MemberMapping mm : s) {
                boolean method = mm instanceof MethodMapping;
                if (!exclude || (exclude && !mm.name.getValue().equals(mm.name.original))) {
                    m.add("\t" + (method ? "METHOD" : "FIELD") + " " + mm.name.original + " " + mm.name.getValue() + " "
                            + mm.desc.original + "\n");
                    include = true;
                }
            }
            if (!exclude || (exclude && include)) {
                bw.write(c);
                for (String sm : m) {
                    bw.write(sm);
                }
            }
        }
        bw.close();
    }

    public File getFile() {
        return file;
    }

    public Map<String, ClassNode> getClassEntries() {
        return classEntries;
    }

    public Map<String, byte[]> getFileEntries() {
        return fileEntries;
    }

    public Mapping getMapping() {
        return mapping;
    }

    private class ConstRedirection {
        public final Usage usage;
        public final int index;
        private final Map<Usage, Integer> uses = new HashMap<>();
        private final Map<String, Integer> types = new HashMap<>();
        private final String initKey;

        ConstRedirection(Usage usage, int index) {
            this.usage = usage;
            this.index = index;
            this.initKey = null;
        }

        public ConstRedirection(Usage usage, int index, String owner, String name, String desc) {
            this.usage = usage;
            this.index = index;
            this.initKey = getKey(usage, owner, name, desc);
            types.put(initKey, index);
        }

        /**
         * Gets the UTF index or creates a new one for usage by the given type.
         * Used for differentiating between basic usages.<br>
         * If the usage intention does not match the declared one, a new
         * constant pool entry is added and the index to it is returned.
         *
         * @param cn
         * @param usage
         * @return
         */
        public int getIndexForValue(ClassNode cn, Usage usage) {
            // No redirection needed if same usage type.
            if (usage.equals(this.usage)) {
                return index;
            }

            // Ensure redirects has redirect for the given type
            if (!uses.containsKey(usage)) {
                int next = cn.constants.size() + 1;
                uses.put(usage, next);
                cn.constants.add(null);
            }
            return uses.get(usage);
        }

        /**
         * Gets the UTF index or creates a new one for usage by the given type.
         * Used for differentiating usage by different members.<br>
         * If the usage intention does not match the declared one, a new
         * constant pool entry is added and the index to it is returned.
         *
         * @param cn
         * @param use
         * @param owner
         * @param name
         * @param desc
         * @return
         */
        public int getIndexForMember(ClassNode cn, Usage use, String owner, String name, String desc) {
            String key = getKey(use, owner, name, desc);
            // No redirection needed if same usage type.
            if (types.containsKey(key)) {
                return types.get(key);
            }

            // Ensure redirects has redirect for the given type
            int next = cn.constants.size() + 1;
            types.put(key, next);
            cn.constants.add(null);
            return types.get(key);
        }

        private String getKey(Usage use, String owner, String name, String desc) {
            return use.name() + "#" + owner + "." + name + "-" + desc;
        }
    }

    private enum Usage {
        STRING, CLASS, MEMBER_NAME, MEMBER_DESC
    }
}

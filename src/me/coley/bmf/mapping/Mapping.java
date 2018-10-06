package me.coley.bmf.mapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.coley.bmf.ClassNode;
import me.coley.bmf.FieldNode;
import me.coley.bmf.MethodNode;
import me.coley.bmf.type.Type;
import me.coley.bmf.type.descriptors.MemberDescriptor;
import me.coley.bmf.util.Box;
import me.coley.bmf.util.ClassLoadStatus;
import me.coley.bmf.util.ImmutableBox;

public class Mapping {
    private final Map<String, ClassMapping> mappings = new HashMap<>();
    private final Map<ClassMapping, ClassMapping> parents = new HashMap<>();
    private final Map<ClassMapping, List<ClassMapping>> interfaces = new HashMap<>();
    private final Map<ClassMapping, List<ClassMapping>> children = new HashMap<>();
    private final List<ClassMapping> inited = new ArrayList<>();
    private final List<String> failedLoads = new ArrayList<>();

    /**
     * Generates a ClassMapping from a given ClassNode. The generated
     * ClassMapping is automatically added to the mappings map.
     * 
     * @param node
     * @return
     */
    public ClassMapping createMappingFromNode(ClassNode node) {
        String name = node.getName();
        ClassMapping cm = new ClassMapping(name);
        mappings.put(name, cm);
        return cm;
    }

    /**
     * Generates a ClassMapping from a given ClassNode. The generated
     * ClassMapping is automatically added to the mappings map. The mapping
     * generated will only update the inner name. Outer name changes will have
     * to be done directly to the outer ClassMapping.
     * 
     * @param outer
     * @param inner
     * @return
     */
    public ClassMapping createMappingFromInnerNode(ClassNode outer, ClassNode inner) {
        String innerName = inner.getName();
        ClassMapping cm = new InnerClassMapping(getClassName(outer.getName()), innerName);
        mappings.put(innerName, cm);
        return cm;
    }

    /**
     * Adds MemberMappings to the ClassMappings associated with the given
     * classnode. The additions are based off of the values inside the given
     * node.
     * 
     * @param node
     * @param isLibrary
     */
    public void createMemberMappings(Map<String, ClassNode> nodes, ClassMapping cm, ClassNode node) {
        if (cm == null) {
            throw new RuntimeException("Could not find the mapping for: " + node.getName());
        }
        // Skip if already done.
        if (inited.contains(cm))
            return;
        // Makes sure superclasses have had their members mapped first.
        // Both parents and interfaces are added.
        List<ClassMapping> supers = new ArrayList<ClassMapping>();
        if (hasParent(cm)) {
            supers.add(parents.get(cm));
        }
        if (hasInterfaces(cm)) {
            supers.addAll(interfaces.get(cm));
        }
        for (ClassMapping parent : supers) {
            if (!inited.contains(parent)) {
                ClassNode parentNode = nodes.get(parent.name.original);
                if (parentNode != null) {
                    ClassMapping classMapping = getMapping(parent.name.original);
                    createMemberMappings(nodes, classMapping, parentNode);
                }
            }
        }
        // Adding the members
        try {
            for (MethodNode mn : node.methods) {
                String namee = mn.getName();
                Box<String> box = new Box<String>(namee);
                cm.addMember(this, new MethodMapping(box, Type.method(this, mn.getDesc())));
                // TODO: add variables to MethodMapping
            }
            for (FieldNode fn : node.fields) {
                String namee = fn.getName();
                Box<String> box = new Box<String>(namee);
                cm.addMember(this, new MemberMapping(box, Type.variable(this, fn.getDesc())));
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to create MemberMapping for member of class: " + node.getName());
        }
        inited.add(cm);
    }

    /**
     * Generates a ClassMapping from a class loaded into the classpath. The
     * generated ClassMapping is automatically added to the mappings map and the
     * parent/child hierarchy is created.
     * 
     * @param name
     *            Class name
     * @return
     */
    public ClassMapping createMappingFromRuntime(String name) {
        // If it didn't work before it probably won't work now.
        if (failedLoads.contains(name))
            return null;
        ClassLoadStatus cls = ClassLoadStatus.getLoadStatus(name);
        if (cls == ClassLoadStatus.NOT_IN_JVM) {
            failedLoads.add(name);
            return null;
        }
        // Load class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(name.replace("/", "."));
        } catch (ClassNotFoundException e) {
            // shouldn't happen since ClassLoadStatus should check for this.
            failedLoads.add(name);
            return null;
        }
        // Create mapping object and add it to the map
        ClassMapping classMapping = new ClassMapping(name);
        mappings.put(name, classMapping);
        // Get the parent if it has one and map that.
        // Then create set the hierarchy structure
        if (clazz != Object.class && clazz.getSuperclass() != null) {
            String parentName = clazz.getSuperclass().getName().replace(".", "/");
            ClassMapping parentMapping = mappings.containsKey(parentName) ? mappings.get(parentName)
                    : createMappingFromRuntime(parentName);
            if (parentMapping != null) {
                setParent(classMapping, parentMapping);
                addChild(parentMapping, classMapping);
            }
        }
        // Get the interfaces and map those as well.
        // Then create set the hierarchy structure
        for (Class<?> interfaceClass : clazz.getInterfaces()) {
            String interfaceNAme = interfaceClass.getName().replace(".", "/");
            ClassMapping interfaceMapping = mappings.containsKey(interfaceNAme) ? mappings.get(interfaceNAme)
                    : createMappingFromRuntime(interfaceNAme);
            if (interfaceMapping != null) {
                addInterface(classMapping, interfaceMapping);
                addChild(interfaceMapping, classMapping);
            }
        }
        // Create member mappings for the constructors
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            classMapping.addMember(this, new MethodMapping(new ImmutableBox<String>("<init>"),
                    Type.method(this, Type.getConstructorDescriptor(constructor))));
        }
        // Create member mappings for the methods
        for (Method m : clazz.getDeclaredMethods()) {
            classMapping.addMember(this, new MethodMapping(new ImmutableBox<String>(m.getName()),
                    Type.method(this, Type.getMethodDescriptor(m))));
        }
        // Create member mappings for the fields
        for (Field m : clazz.getDeclaredFields()) {
            classMapping.addMember(this, new MemberMapping(new ImmutableBox<String>(m.getName()),
                    Type.variable(this, Type.getFieldDescriptor(m))));
        }
        return classMapping;
    }

    /**
     * Generates a class mapping for a class by the given name. It will not be
     * able to be renamed.
     * 
     * @param name
     * @return
     */
    public ClassMapping createImmutableMapping(String name) {
        ClassMapping classMapping = new ClassMapping(new ImmutableBox<String>(name));
        mappings.put(name, classMapping);
        return classMapping;
    }

    /**
     * Retrieves the Boxed name of a class. Throws an exception if the boxed
     * name could not be located.
     * 
     * @param name
     *            Class name
     * 
     * @return
     */
    public ClassMapping getOrCreateClassMapping(String name) {
        // Get existing mapping if possible
        if (hasClass(name)) {
            return mappings.get(name);
        } else {
            // Attempt to load the mapping from the classpath
            ClassMapping cm = createMappingFromRuntime(name);
            if (cm != null)
                return cm;
            // Well, can't say we didn't try.
            return createImmutableMapping(name);
        }
    }

    /**
     * Retrieves the Boxed name of a class based on its original name.
     * 
     * @param originalName
     *            Original name of the class.
     * @return
     */
    public Box<String> getClassName(String originalName) {
        ClassMapping cm = getOrCreateClassMapping(originalName);
        return cm == null ? null : cm.name;
    }

    /**
     * Adds a ClassMapping to the mappings map.
     * 
     * @param mapping
     */
    public void addMapping(ClassMapping mapping) {
        mappings.put(mapping.name.original, mapping);
    }

    /**
     * Retrieves a ClassMapping from the mappings map.
     * 
     * @param name
     *            The original name of the class. The value will be the
     *            equivalent of <i>ClassMapping.original</i>.
     * 
     * @return
     */
    public ClassMapping getMapping(String name) {
        return mappings.get(name);
    }

    /**
     * Retrieves a ClassMapping from the mappings map based on a given class.
     * 
     * @param node
     */
    public ClassMapping getMapping(ClassNode node) {
        return getMapping(node.getName());
    }

    /**
     * Checks if a mappings map has a given entry.
     * 
     * @param name
     * @return
     */
    public boolean hasClass(String name) {
        return mappings.containsKey(name);
    }

    /**
     * Returns the mappings map.
     * 
     * @return
     */
    public Map<String, ClassMapping> getMappings() {
        return mappings;
    }

    // ------------------------------------------- //
    // ------------------------------------------- //

    /**
     * Retrieves a MemberDescriptor given a class's name and the member
     * descriptor and name.
     * 
     * @param className
     * @param name
     *            Member's name
     * @param desc
     *            Member's desc
     * @return <i>null</i> if not such member exists
     */
    public MemberDescriptor getDesc(String className, String name, String desc) {
        ClassMapping cm = getMapping(className);
        if (cm != null) {
            for (MemberMapping mm : cm.getMembers()) {
                if (mm.desc.original.equals(desc) && mm.name.original.equals(name)) {
                    return mm.desc;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a list of MemberDescriptors given a class's name and the member
     * descriptor and name.
     * 
     * @param className
     * @param desc
     * @return
     */
    public List<MemberDescriptor> getDescs(String className, String desc) {
        List<MemberDescriptor> list = new ArrayList<MemberDescriptor>();
        ClassMapping cm = getMapping(className);
        if (cm != null) {
            for (MemberMapping mm : cm.getMembers()) {
                if (mm.desc.original.equals(desc)) {
                    list.add(mm.desc);
                }
            }
        }
        return list;
    }

    /**
     * Checks if a member of the given name and desc are found within mappings
     * for the class with the given name.
     * 
     * To cut down on execution time you could just use getDesc(String, String,
     * String) and check if the local is null.
     * 
     * @param className
     * @param name
     * @param desc
     * @return
     */
    public boolean hasDesc(String className, String name, String desc) {
        if (hasClass(className)) {
            ClassMapping cm = getMapping(className);
            for (MemberMapping mm : cm.getMembers()) {
                if (mm.desc.original.equals(desc) && mm.name.original.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ------------------------------------------- //
    // ------------------------------------------- //

    /**
     * Checks if a superclass has a member matching the given one. If so it will
     * return that one. Else it will return the parameter.
     * 
     * @param mm
     * @return
     */
    public MemberMapping getMemberInstance(ClassMapping owner, MemberMapping mm) {
        if (hasParent(owner)) {
            ClassMapping parent = parents.get(owner);
            MemberMapping mp = getMatch(parent, mm);
            if (mp != null) {
                return mp;
            }
        }
        if (hasInterfaces(owner)) {
            for (ClassMapping parent : interfaces.get(owner)) {
                MemberMapping mi = getMatch(parent, mm);
                if (mi != null) {
                    return mi;
                }
            }
        }
        return mm;
    }

    /**
     * Iterative search for a member matching the details of the given one.
     * 
     * @param owner
     * @param mm
     * @return
     */
    private MemberMapping getMatch(ClassMapping owner, MemberMapping mm) {
        if (hasParent(owner)) {
            ClassMapping parent = parents.get(owner);
            MemberMapping mp = getMatch(parent, mm);
            if (mp != null) {
                return mp;
            }
        }
        if (hasInterfaces(owner)) {
            for (ClassMapping inter : interfaces.get(owner)) {
                MemberMapping mi = getMatch(inter, mm);
                if (mi != null) {
                    return mi;
                }
            }
        }
        for (MemberMapping m : owner.getMembers()) {
            if (m.desc.original.equals

            (mm.desc.original) && m.name.original.equals(mm.name.original)) {
                return m;
            }
        }
        return null;
    }

    // ------------------------------------------- //
    // ------------------------------------------- //

    /**
     * Returns the first <i>(the only for non-interfaces)</i> parent of a given
     * ClassMapping.
     * 
     * @param child
     * @return
     */
    public ClassMapping getParent(ClassMapping child) {
        if (!hasParent(child))
            return null;
        return parents.get(child);
    }

    /**
     * Maps a parent to the given child mapping.
     * 
     * @param child
     * @param parent
     */
    public void setParent(ClassMapping child, ClassMapping parent) {
        parents.put(child, parent);
    }

    /**
     * Checks if a given class has parents.
     * 
     * @param child
     * @return
     */
    public boolean hasParent(ClassMapping child) {
        return parents.containsKey(child);
    }

    // ------------------------------------------- //
    // ------------------------------------------- //

    /**
     * Returns a list of interfaces of a given ClassMapping.
     * 
     * @param child
     * @return
     */
    public List<ClassMapping> getInterfaces(ClassMapping child) {
        return interfaces.get(child);
    }

    /**
     * Maps an interface to the given child mapping.
     * 
     * @param child
     * @param interfacee
     */
    public void addInterface(ClassMapping child, ClassMapping interfacee) {
        if (!hasInterfaces(child)) {
            interfaces.put(child, new ArrayList<ClassMapping>());
        }
        interfaces.get(child).add(interfacee);
    }

    /**
     * Checks if a given class has interfaces.
     * 
     * @param child
     * @return
     */
    public boolean hasInterfaces(ClassMapping child) {
        return interfaces.containsKey(child);
    }

    // ------------------------------------------- //
    // ------------------------------------------- //

    /**
     * Returns a list of classes that extend the given class <i>(parent)</i>.
     * 
     * @param parent
     * @return
     */
    public List<ClassMapping> getChildren(ClassMapping parent) {
        return children.get(parent);
    }

    /**
     * Adds a child link to the given parent mapping.
     * 
     * @param parent
     * @param child
     */
    public void addChild(ClassMapping parent, ClassMapping child) {
        if (!hasChildren(parent)) {
            children.put(parent, new ArrayList<ClassMapping>());
        }
        children.get(parent).add(child);
    }

    /**
     * Checks if a given class has children.
     * 
     * @param parent
     * @return
     */
    public boolean hasChildren(ClassMapping parent) {
        return children.containsKey(parent);
    }

    // ------------------------------------------- //
    // ------------------------------------------- //

    /**
     * Retrieves a MemberMapping from a given ClassMapping. If one cannot be
     * found the parents/interfaces of the given class are searched.
     * 
     * @param owner
     * @param name
     * @param desc
     * @return
     */
    public MemberMapping getMemberMapping(ClassMapping owner, String name, String desc) {
        return getMemberMapping(owner, name, desc, true);
    }

    /**
     * Retrieves a MemberMapping from a given ClassMapping. If one cannot be
     * found the parents/interfaces of the given class can be searched.
     * 
     * @param owner
     * @param name
     * @param desc
     * @param recursive
     *            Search parents and interfaces recursively
     * @return
     */
    public MemberMapping getMemberMapping(ClassMapping owner, String name, String desc, boolean recursive) {
        // Fail
        if (owner == null)
            return null;
        // Check given class.
        // If no recursion is allowed the value will be returned even if null.
        MemberMapping m = owner.getMemberMapping(name, desc);
        if (m != null || !recursive)
            return m;
        // Check parents
        if (parents.containsKey(owner)) {
            ClassMapping parentMapping = parents.get(owner);
            MemberMapping mp = getMemberMapping(parentMapping, name, desc, true);
            if (mp != null)
                return mp;
        }
        // Check interfaces
        if (interfaces.containsKey(owner)) {
            for (ClassMapping interfaceMapping : interfaces.get(owner)) {
                MemberMapping mi = getMemberMapping(interfaceMapping, name, desc, true);
                if (mi != null)
                    return mi;
            }
        }
        // Poop
        return null;
    }
}

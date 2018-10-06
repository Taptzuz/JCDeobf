package me.coley.bmf.signature;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.mapping.InnerClassMapping;
import me.coley.bmf.mapping.Mapping;
import me.coley.bmf.util.Box;
import me.coley.bmf.util.ImmutableBox;

public abstract class Signature {
    protected TypeArgHelper helper;
    protected SigArg type;

    public abstract String toSignature();

    /**
     * Reads a local variable or field's signature.
     * 
     * @param mapping
     * @param sig
     *            Variable or field's signature.
     * @return
     */
    public static Signature read(Mapping mapping, String sig) {
        int len = sig.length();
        int pos;
        char c;
        TypeArgHelper helper = new TypeArgHelper();
        // Parse type arguments
        // Example: <T:Ljava/lang/Object;>
        if (sig.charAt(0) == '<') {
            pos = 2;
            do {
                int end = sig.indexOf(':', pos);
                // Parse arguments name.
                // From the example this would be 'T'
                String name = sig.substring(pos - 1, end);
                pos = end + 1;
                // Parse type arguments value
                c = sig.charAt(pos);
                if (c == 'L' || c == '[' || c == 'T') {
                    SigArg s = parseType(mapping, sig, pos);
                    helper.addType(name, s);
                    pos += s.length();
                }
                // Remaining size implies existence of interfaces.
                // Parse interfaces.
                while ((c = sig.charAt(pos++)) == ':') {
                    SigArg s = parseType(mapping, sig, pos);
                    helper.addInterface(name, s);
                    pos += s.length();
                }
            } while (c != '>');
        } else {
            pos = 0;
        }
        // Parse method signature
        if (sig.charAt(pos) == '(') {
            pos++;
            // Parse parameters if any exist
            List<SigArg> parameters = (sig.charAt(pos) != ')') ? new ArrayList<>() : null;
            while (sig.charAt(pos) != ')') {
                SigArg param = parseType(mapping, sig, pos);
                parameters.add(param);
                pos += param.length();
            }
            // Parse return type
            SigArg retType = parseType(mapping, sig, pos + 1);
            pos += retType.length() + 1;
            // Remaining size implies existence of exceptions.
            // Parse exceptions.
            while (pos < len) {
                // TODO:
                // https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3.4
                // Parse exceptions, I've never seen a sample with them though.
                SigArg s2 = parseType(mapping, sig, pos);
                System.err.println("Exceptions: " + s2.toArg());
                pos += s2.length();
            }
            // Combine, compare to input and return if matched.
            MethodSignature ts = new MethodSignature(helper, parameters, retType);
            if (!ts.toSignature().equals(sig)) {
                throw new RuntimeException("Invalid parse of: " + sig + " --(gave)--> " + ts.toSignature());
            }
            return ts;
        } // Parse field signature
        else {
            // Parse type
            SigArg s = parseType(mapping, sig, pos);
            pos += s.length();
            // Remaining size implies existence of interfaces.
            // Parse interfaces if any exist.
            List<SigArg> interfaces = (pos < len) ? new ArrayList<>() : null;
            while (pos < len) {
                SigArg s2 = parseType(mapping, sig, pos);
                interfaces.add(s2);
                pos += s2.length();
            }
            // Combine, compare to input and return if matched.
            TypeSignature ts = new TypeSignature(s, helper, interfaces);
            if (!ts.toSignature().equals(sig)) {
                throw new RuntimeException("Invalid parse of: " + sig + " --(gave)--> " + ts.toSignature());
            }
            return ts;
        }
    }

    private static SigArg parseType(final Mapping mapping, final String sig, int pos) {
        char c;
        int start, outerStart, end, genericsStart = -1;
        boolean inner;
        List<SigArg> generics = null;
        switch (c = sig.charAt(pos++)) {
        case 'Z':
        case 'C':
        case 'B':
        case 'S':
        case 'I':
        case 'F':
        case 'J':
        case 'D':
        case 'V':
            return new SigArgPrimitive(String.valueOf(c));
        case '[':
            return new SigArgArray(parseType(mapping, sig, pos));
        case 'T':
            end = sig.indexOf(';', pos);
            return new SigArgGeneric(sig.substring(pos, end));
        case 'L':
            start = pos - 1;
            outerStart = start;
            inner = false;
            while (true) {
                switch (c = sig.charAt(pos++)) {
                case '.':
                case ';':
                    if (c == ';') {
                        if (inner) {
                            if (generics == null) {
                                genericsStart = pos - 3;
                            }
                            String outerName = sig.substring(outerStart + 1, genericsStart);
                            String innerName = outerName + "$" + sig.substring(start, pos - 1);
                            Box<String> boxOuter = mapping.getClassName(outerName);
                            if (boxOuter == null) {
                                boxOuter = new ImmutableBox<String>(outerName);
                            }
                            Box<String> boxInner = mapping.getClassName(innerName);
                            if (boxInner == null) {
                                boxInner = new ImmutableBox<String>(outerName);
                            }
                            if (boxInner instanceof InnerClassMapping.InnerClassBox) {
                                return new SigArgClassWithInner(boxOuter, (InnerClassMapping.InnerClassBox) boxInner,
                                        generics);
                            }
                            throw new RuntimeException("Could not find inner mappings for: " + innerName);
                        } else {
                            if (generics == null) {
                                genericsStart = pos - 1;
                            }
                            String value = sig.substring(start, genericsStart);
                            Box<String> box = mapping.getClassName(value);
                            if (box == null) {
                                box = new ImmutableBox<String>(value);
                            }
                            return new SigArgClass(box, generics);
                        }

                    }
                    outerStart = start;
                    start = pos;
                    inner = true;
                    break;
                case '<':
                    generics = new ArrayList<>();
                    genericsStart = pos - 1;
                    boolean loop = true;
                    while (loop) {
                        switch (c = sig.charAt(pos)) {
                        case '>':
                            loop = false;
                            break;
                        case '*':
                            ++pos;
                            generics.add(new SigArgWild(Character.toString(c), null));
                            break;
                        case '+':
                        case '-': {
                            SigArg s = parseType(mapping, sig, pos + 1);
                            pos += s.length() + 1;
                            generics.add(new SigArgWild(Character.toString(c), s));
                            break;
                        }
                        case '=': {
                            SigArg s = parseType(mapping, sig, pos);
                            pos += s.length();
                            generics.add(new SigArgWild(Character.toString(c), s));
                            break;
                        }
                        default: {
                            SigArg s = parseType(mapping, sig, pos);
                            pos += s.length();
                            generics.add(s);
                            break;
                        }
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Failed to parse: " + sig + " @ " + pos + " : " + sig.substring(pos));
    }
}

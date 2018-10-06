package me.coley.bmf.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.mapping.Mapping;
import me.coley.bmf.type.descriptors.MethodDescriptor;
import me.coley.bmf.type.descriptors.VariableDescriptor;
import me.coley.bmf.util.ImmutableBox;

public abstract class Type {
    public static final PrimitiveType BYTE = new PrimitiveType(Sort.BYTE, "B");
    public static final PrimitiveType CHAR = new PrimitiveType(Sort.CHAR, "C");
    public static final PrimitiveType DOUBLE = new PrimitiveType(Sort.DOUBLE, "D");
    public static final PrimitiveType FLOAT = new PrimitiveType(Sort.FLOAT, "F");
    public static final PrimitiveType INT = new PrimitiveType(Sort.INT, "I");
    public static final PrimitiveType LONG = new PrimitiveType(Sort.LONG, "J");
    public static final PrimitiveType SHORT = new PrimitiveType(Sort.SHORT, "S");
    public static final PrimitiveType BOOLEAN = new PrimitiveType(Sort.BOOLEAN, "Z");
    public static final PrimitiveType VOID = new PrimitiveType(Sort.VOID, "V");
    public static final ClassType OBJECT = new ClassType(new ImmutableBox<String>("Ljava/lang/Object;"));
    public final int sort;

    public Type(int sort) {
        this.sort = sort;
    }

    abstract public String toDesc();

    /**
     * Creates a VariableDescriptor given the Mapping source and member
     * descriptor. Used for both fields and local variables.
     * 
     * @param mapping
     * @param desc
     * @return
     */
    public static VariableDescriptor variable(Mapping mapping, String desc) {
        char[] carr = desc.toCharArray();
        Type type = null;
        int i = 0;
        while (true) {
            char c = carr[i];
            if (c == '[') {
                type = readArray(mapping, desc, i + 1);
                break;
            } else if (c == 'L') {
                int len = 1;
                while (c != ';') {
                    c = carr[i + len];
                    len++;
                }
                type = (type(mapping, desc, i, len));
                break;
            } else {
                Type prim = readPrim(c);
                if (prim != null) {
                    type = prim;
                    break;
                }
            }
            i++;
        }
        return new VariableDescriptor(type);
    }

    /**
     * Creates a MethodDescriptor given the Mapping source and member
     * descriptor.
     * 
     * @param mapping
     * @param desc
     * @return
     */
    public static MethodDescriptor method(Mapping mapping, String desc) {
        char[] carr = desc.toCharArray();
        List<Type> types = new ArrayList<Type>();
        int i = 1;
        while (true) {
            char c = carr[i];
            if (c == ')') {
                break;
            }
            if (c == '[') {
                ArrayType array = readArray(mapping, desc, i + 1);
                types.add(array);
                i += array.toDesc().length() - 1;
            } else if (c == 'L') {
                int len = 1;
                while (c != ';') {
                    c = carr[i + len];
                    len++;
                }
                types.add(type(mapping, desc, i, len));
                i += len - 1;
            } else {
                Type prim = readPrim(c);
                if (prim != null) {
                    types.add(prim);
                }
            }
            i++;
        }
        return new MethodDescriptor(desc, types, type(mapping, desc.substring(desc.indexOf(')') + 1)));
    }

    private static ArrayType readArray(Mapping mapping, String desc, int i) {
        char[] carr = desc.toCharArray();
        Type type = null;
        switch (carr[i]) {
        case '[':
            type = readArray(mapping, desc, i + 1);
            break;
        case 'L':
            int len = 1;
            char c = carr[i];
            while (c != ';') {
                c = carr[i + len];
                len++;
            }
            type = type(mapping, desc, i, len);
            i += len - 1;
            break;
        }
        if (type == null) {
            type = readPrim(carr[i]);
        }
        return new ArrayType(type);
    }

    public static Type readPrim(char c) {
        switch (c) {
        case 'B':
            return BYTE;
        case 'C':
            return CHAR;
        case 'D':
            return DOUBLE;
        case 'F':
            return FLOAT;
        case 'I':
            return INT;
        case 'J':
            return LONG;
        case 'S':
            return SHORT;
        case 'Z':
            return BOOLEAN;
        case 'V':
            return VOID;
        default:
            break;
        }
        return null;
    }

    private static Type type(Mapping mapping, String desc) {
        if (desc.length() == 1) {
            return readPrim(desc.charAt(0));
        } else if (desc.charAt(0) == '[') {
            return readArray(mapping, desc, 1);
        }

        String name = desc.substring(1, desc.length() - 1);
        return new ClassType(mapping.getClassName(name));
    }

    private static Type type(Mapping mapping, String desc, int i, int len) {
        String name = desc.substring(i + 1, (i + len) - 1);
        return new ClassType(mapping.getClassName(name));
    }

    /**
     * Get the type-descriptor from the reflection method.
     * 
     * @param method
     * @return
     */
    public static String getMethodDescriptor(Method method) {
        String s = "(";
        for (final Class<?> c : method.getParameterTypes())
            s += getDescriptorForClass(c);
        s += ')';
        return s + getDescriptorForClass(method.getReturnType());
    }

    /**
     * Get the type-descriptor from the reflection field.
     * 
     * @param field
     * @return
     */
    public static String getFieldDescriptor(Field field) {
        return getDescriptorForClass(field.getType());
    }

    public static String getConstructorDescriptor(Constructor<?> m) {
        String s = "(";
        for (final Class<?> c : m.getParameterTypes())
            s += getDescriptorForClass(c);
        s += ')';
        return s + "V";
    }

    /**
     * <a href=
     * "http://stackoverflow.com/questions/32148846/get-java-field-and-method-descriptors-at-runtime">
     * From Stackoverflow</a>
     * 
     * @param c
     * @return
     */
    public static String getDescriptorForClass(final Class<?> c) {
        if (c.isPrimitive()) {
            if (c == byte.class)
                return "B";
            if (c == char.class)
                return "C";
            if (c == double.class)
                return "D";
            if (c == float.class)
                return "F";
            if (c == int.class)
                return "I";
            if (c == long.class)
                return "J";
            if (c == short.class)
                return "S";
            if (c == boolean.class)
                return "Z";
            if (c == void.class)
                return "V";
            throw new RuntimeException("Unrecognized primitive " + c);
        }
        if (c.isArray())
            return c.getName().replace('.', '/');
        return ('L' + c.getName() + ';').replace('.', '/');
    }
}

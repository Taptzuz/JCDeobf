package me.coley.bmf.type;

public class PrimitiveType extends Type {

    public final String desc;

    public PrimitiveType(int type, String desc) {
        super(type);
        this.desc = desc;
    }

    @Override
    public String toDesc() {
        return desc;
    }

    public String toJavaName() {
        switch (desc.charAt(0)) {
        case 'B':
            return "byte";
        case 'C':
            return "char";
        case 'D':
            return "double";
        case 'F':
            return "float";
        case 'I':
            return "int";
        case 'J':
            return "long";
        case 'S':
            return "short";
        case 'Z':
            return "boolean";
        case 'V':
            return "void";
        }
        return null;
    }

    public static PrimitiveType getFromChar(char ch) {
        switch (ch) {
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
        }
        return null;
    }

}
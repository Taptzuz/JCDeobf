package me.coley.bmf.type;

public class ArrayType extends Type {
    public final Type itemType;

    public ArrayType(Type itemType) {
        super(Sort.ARRAY);
        this.itemType = itemType;
    }

    public int getDepth() {
        int i = 1;
        Type next = itemType;
        while (next.sort == Sort.ARRAY) {
            next = ((ArrayType) next).itemType;
            i++;
        }
        return i;
    }

    @Override
    public String toDesc() {
        return "[" + itemType.toDesc();
    }
}

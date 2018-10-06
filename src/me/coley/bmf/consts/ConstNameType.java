package me.coley.bmf.consts;

public class ConstNameType extends Constant<Integer> {
    private int descIndex;

    public ConstNameType(int nameIndex, int descIndex) {
        super(ConstantType.NAME_TYPE, nameIndex);
        this.descIndex = descIndex;
    }

    public int getNameIndex() {
        return this.getValue();
    }

    public void setNameIndex(int nameIndex) {
        this.setValue(nameIndex);
    }

    public int getDescIndex() {
        return descIndex;
    }

    public void setDescIndex(int index) {
        this.descIndex = index;
    }
}

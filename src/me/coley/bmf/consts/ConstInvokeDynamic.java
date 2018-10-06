package me.coley.bmf.consts;

public class ConstInvokeDynamic extends Constant<Integer> {
    private int nameType;

    public ConstInvokeDynamic(int attribute, int nameType) {
        super(ConstantType.INVOKEDYNAMIC, attribute);
        this.nameType = nameType;
    }

    public int getBootstrapAttribute() {
        return this.getValue();
    }

    public int getNameTypeIndex() {
        return nameType;
    }

    public void setBootstrapAttribute(int index) {
        this.setValue(index);
    }

    public void setNameTypeIndex(int index) {
        this.nameType = index;
    }
}

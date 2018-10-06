package me.coley.bmf.consts;

public class AbstractMemberConstant extends Constant<Integer> {
    private int nameType;

    public AbstractMemberConstant(ConstantType type, int clazz, int nameType) {
        super(type, clazz);
        this.nameType = nameType;
    }

    public int getClassIndex() {
        return super.getValue();
    }

    public int getNameTypeIndex() {
        return nameType;
    }

    public void setClassIndex(int index) {
        this.setValue(index);
    }

    public void setNameTypeIndex(int index) {
        this.nameType = index;
    }
}

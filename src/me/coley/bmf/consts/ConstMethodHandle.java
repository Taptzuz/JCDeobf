package me.coley.bmf.consts;

public class ConstMethodHandle extends Constant<Integer> {
    private int kind;

    public ConstMethodHandle(int kind, int index) {
        super(ConstantType.METHOD_HANDLE, index);
        this.kind = kind;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getIndex() {
        return getValue();
    }

    public void setIndex(int index) {
        this.setValue(index);
    }
}

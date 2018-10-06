package me.coley.bmf.consts;

public class ConstUTF8 extends Constant<String> {
    public ConstUTF8() {
        this(null);
    }

    public ConstUTF8(String data) {
        super(ConstantType.UTF8, data);
    }
}

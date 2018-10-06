package me.coley.bmf.attribute.method;

import me.coley.bmf.util.Measurable;

public class LocalVariableType implements Measurable {
    public int start;
    public int length;
    public int name;
    public int signature;
    public int index;

    public LocalVariableType(int start, int len, int name, int signature, int index) {
        this.start = start;
        this.length = len;
        this.name = name;
        this.signature = signature;
        this.index = index;
    }

    @Override
    public int getLength() {
        // u2: start_pc
        // u2: length
        // u2: name_index
        // u2: signature_index
        // u2: index
        return 10;
    }
}

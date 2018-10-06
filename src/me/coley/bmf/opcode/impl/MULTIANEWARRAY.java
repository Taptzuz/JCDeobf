package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractClassPointer;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class MULTIANEWARRAY extends AbstractClassPointer {
    public int dimensions;

    public MULTIANEWARRAY(int classIndex, int dimensions) {
        super(OpcodeType.ARRAY, Opcode.ANEWARRAY, 3, classIndex);
        this.dimensions = dimensions;
    }
}

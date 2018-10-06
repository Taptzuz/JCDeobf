package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractClassPointer;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class ANEWARRAY extends AbstractClassPointer {

    public ANEWARRAY(int classIndex) {
        super(OpcodeType.ARRAY, Opcode.ANEWARRAY, 3, classIndex);
    }
}

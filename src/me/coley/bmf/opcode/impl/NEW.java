package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractClassPointer;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class NEW extends AbstractClassPointer {

    public NEW(int classIndex) {
        super(OpcodeType.ALLOCATION, Opcode.NEW, 3,classIndex);
    }
}

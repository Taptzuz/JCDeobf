package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractClassPointer;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class CHECKCAST extends AbstractClassPointer {

    public CHECKCAST(int classIndex) {
        super(OpcodeType.TYPE_CONVERSION, Opcode.CHECKCAST, 3, classIndex);
    }
}

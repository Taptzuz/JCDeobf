package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractClassPointer;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class INSTANCEOF extends AbstractClassPointer {

    public INSTANCEOF(int classIndex) {
        super(OpcodeType.TYPE_CONVERSION, Opcode.INSTANCEOF, 3,classIndex);
    }
}

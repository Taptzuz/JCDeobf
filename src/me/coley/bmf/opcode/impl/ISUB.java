package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class ISUB extends Opcode {
    public ISUB() {
        super(OpcodeType.MATH, Opcode.ISUB, 1);
    }
}

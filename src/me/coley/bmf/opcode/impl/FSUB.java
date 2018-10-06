package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FSUB extends Opcode {
    public FSUB() {
        super(OpcodeType.MATH, Opcode.FSUB, 1);
    }
}

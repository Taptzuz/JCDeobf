package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FMUL extends Opcode {
    public FMUL() {
        super(OpcodeType.MATH, Opcode.FMUL, 1);
    }
}

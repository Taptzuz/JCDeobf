package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LMUL extends Opcode {
    public LMUL() {
        super(OpcodeType.MATH, Opcode.LMUL, 1);
    }
}

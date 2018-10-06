package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IMUL extends Opcode {
    public IMUL() {
        super(OpcodeType.MATH, Opcode.IMUL, 1);
    }
}

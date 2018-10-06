package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DMUL extends Opcode {
    public DMUL() {
        super(OpcodeType.MATH, Opcode.DMUL, 1);
    }
}

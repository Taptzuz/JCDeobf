package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FADD extends Opcode {
    public FADD() {
        super(OpcodeType.MATH, Opcode.FADD, 1);
    }
}

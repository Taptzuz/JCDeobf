package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FNEG extends Opcode {
    public FNEG() {
        super(OpcodeType.MATH, Opcode.FNEG, 1);
    }
}

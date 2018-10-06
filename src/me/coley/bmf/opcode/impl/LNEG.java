package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LNEG extends Opcode {
    public LNEG() {
        super(OpcodeType.MATH, Opcode.LNEG, 1);
    }
}

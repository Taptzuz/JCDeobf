package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LSHR extends Opcode {
    public LSHR() {
        super(OpcodeType.MATH, Opcode.LSHR, 1);
    }
}

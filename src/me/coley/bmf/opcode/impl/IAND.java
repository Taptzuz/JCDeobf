package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IAND extends Opcode {
    public IAND() {
        super(OpcodeType.MATH, Opcode.IAND, 1);
    }
}

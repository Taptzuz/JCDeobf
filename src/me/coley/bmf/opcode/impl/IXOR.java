package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IXOR extends Opcode {
    public IXOR() {
        super(OpcodeType.MATH, Opcode.IXOR, 1);
    }
}

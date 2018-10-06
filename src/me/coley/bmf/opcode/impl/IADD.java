package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IADD extends Opcode {
    public IADD() {
        super(OpcodeType.MATH, Opcode.IADD, 1);
    }
}

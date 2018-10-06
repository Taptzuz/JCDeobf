package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LADD extends Opcode {
    public LADD() {
        super(OpcodeType.MATH, Opcode.LADD, 1);
    }
}

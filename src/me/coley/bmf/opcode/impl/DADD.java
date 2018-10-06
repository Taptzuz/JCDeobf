package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DADD extends Opcode {
    public DADD() {
        super(OpcodeType.MATH, Opcode.DADD, 1);
    }
}

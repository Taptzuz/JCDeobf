package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DSUB extends Opcode {
    public DSUB() {
        super(OpcodeType.MATH, Opcode.DSUB, 1);
    }
}

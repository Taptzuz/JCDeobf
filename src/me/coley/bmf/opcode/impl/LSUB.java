package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LSUB extends Opcode {
    public LSUB() {
        super(OpcodeType.MATH, Opcode.LSUB, 1);
    }
}

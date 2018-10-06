package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LSHL extends Opcode {
    public LSHL() {
        super(OpcodeType.MATH, Opcode.LSHL, 1);
    }
}

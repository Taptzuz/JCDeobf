package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DREM extends Opcode {
    public DREM() {
        super(OpcodeType.MATH, Opcode.DREM, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class INEG extends Opcode {
    public INEG() {
        super(OpcodeType.MATH, Opcode.INEG, 1);
    }
}

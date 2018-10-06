package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DNEG extends Opcode {
    public DNEG() {
        super(OpcodeType.MATH, Opcode.DNEG, 1);
    }
}

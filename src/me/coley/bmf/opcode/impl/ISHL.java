package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class ISHL extends Opcode {
    public ISHL() {
        super(OpcodeType.MATH, Opcode.ISHL, 1);
    }
}

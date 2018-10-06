package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LOR extends Opcode {
    public LOR() {
        super(OpcodeType.MATH, Opcode.LOR, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DDIV extends Opcode {
    public DDIV() {
        super(OpcodeType.MATH, Opcode.DDIV, 1);
    }
}

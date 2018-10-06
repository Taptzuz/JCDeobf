package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FDIV extends Opcode {
    public FDIV() {
        super(OpcodeType.MATH, Opcode.FDIV, 1);
    }
}

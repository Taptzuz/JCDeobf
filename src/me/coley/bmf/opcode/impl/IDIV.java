package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IDIV extends Opcode {
    public IDIV() {
        super(OpcodeType.MATH, Opcode.IDIV, 1);
    }
}

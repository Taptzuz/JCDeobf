package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LDIV extends Opcode {
    public LDIV() {
        super(OpcodeType.MATH, Opcode.LDIV, 1);
    }
}

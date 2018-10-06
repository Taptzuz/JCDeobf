package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IUSHR extends Opcode {
    public IUSHR() {
        super(OpcodeType.MATH, Opcode.IUSHR, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class ISHR extends Opcode {
    public ISHR() {
        super(OpcodeType.MATH, Opcode.ISHR, 1);
    }
}

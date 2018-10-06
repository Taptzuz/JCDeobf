package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LUSHR extends Opcode {
    public LUSHR() {
        super(OpcodeType.MATH, Opcode.LUSHR, 1);
    }
}

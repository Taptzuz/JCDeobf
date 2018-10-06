package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LXOR extends Opcode {
    public LXOR() {
        super(OpcodeType.MATH, Opcode.LXOR, 1);
    }
}

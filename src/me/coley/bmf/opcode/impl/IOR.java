package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IOR extends Opcode {
    public IOR() {
        super(OpcodeType.MATH, Opcode.IOR, 1);
    }
}

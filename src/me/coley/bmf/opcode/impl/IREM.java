package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IREM extends Opcode {
    public IREM() {
        super(OpcodeType.MATH, Opcode.IREM, 1);
    }
}

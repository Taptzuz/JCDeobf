package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LAND extends Opcode {
    public LAND() {
        super(OpcodeType.MATH, Opcode.LAND, 1);
    }
}

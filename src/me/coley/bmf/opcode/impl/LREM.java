package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LREM extends Opcode {
    public LREM() {
        super(OpcodeType.MATH, Opcode.LREM, 1);
    }
}

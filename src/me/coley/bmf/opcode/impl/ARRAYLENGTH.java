package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class ARRAYLENGTH extends Opcode {
    public ARRAYLENGTH() {
        super(OpcodeType.ARRAY, Opcode.ARRAYLENGTH, 1);
    }
}

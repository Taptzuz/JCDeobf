package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LCMP extends Opcode {
    public LCMP() {
        super(OpcodeType.STACK, Opcode.LCMP, 1);
    }
}

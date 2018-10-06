package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class POP extends Opcode {
    public POP() {
        super(OpcodeType.STACK, Opcode.POP, 1);
    }
}

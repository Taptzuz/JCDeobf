package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DUP extends Opcode {
    public DUP() {
        super(OpcodeType.STACK, Opcode.DUP, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DUP2 extends Opcode {
    public DUP2() {
        super(OpcodeType.STACK, Opcode.DUP2, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DUP2_X1 extends Opcode {
    public DUP2_X1() {
        super(OpcodeType.STACK, Opcode.DUP2_X1, 1);
    }
}

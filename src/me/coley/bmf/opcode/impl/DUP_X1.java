package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DUP_X1 extends Opcode {
    public DUP_X1() {
        super(OpcodeType.STACK, Opcode.DUP_X1, 1);
    }
}

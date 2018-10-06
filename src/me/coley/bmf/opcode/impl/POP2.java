package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class POP2 extends Opcode {
    public POP2() {
        super(OpcodeType.STACK, Opcode.POP2, 1);
    }
}

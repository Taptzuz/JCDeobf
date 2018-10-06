package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FCMPG extends Opcode {
    public FCMPG() {
        super(OpcodeType.STACK, Opcode.FCMPG, 1);
    }
}

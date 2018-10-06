package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class NOP extends Opcode {
    public NOP() {
        super(OpcodeType.OTHER, Opcode.NOP, 1);
    }
}

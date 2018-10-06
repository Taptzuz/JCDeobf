package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IALOAD extends Opcode {
    public IALOAD() {
        super(OpcodeType.ARRAY, Opcode.IALOAD, 1);
    }
}

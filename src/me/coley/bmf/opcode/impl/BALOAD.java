package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class BALOAD extends Opcode {
    public BALOAD() {
        super(OpcodeType.ARRAY, Opcode.BALOAD, 1);
    }
}

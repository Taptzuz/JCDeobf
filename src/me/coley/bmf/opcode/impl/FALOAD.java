package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FALOAD extends Opcode {
    public FALOAD() {
        super(OpcodeType.ARRAY, Opcode.FALOAD, 1);
    }
}

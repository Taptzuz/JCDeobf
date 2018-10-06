package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DALOAD extends Opcode {
    public DALOAD() {
        super(OpcodeType.ARRAY, Opcode.DALOAD, 1);
    }
}

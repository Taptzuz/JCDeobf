package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class SALOAD extends Opcode {
    public SALOAD() {
        super(OpcodeType.ARRAY, Opcode.SALOAD, 1);
    }
}

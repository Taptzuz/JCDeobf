package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class SASTORE extends Opcode {
    public SASTORE() {
        super(OpcodeType.ARRAY, Opcode.SASTORE, 1);
    }
}

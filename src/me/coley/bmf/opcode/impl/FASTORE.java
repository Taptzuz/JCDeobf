package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FASTORE extends Opcode {
    public FASTORE() {
        super(OpcodeType.ARRAY, Opcode.FASTORE, 1);
    }
}

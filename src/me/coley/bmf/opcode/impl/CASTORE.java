package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class CASTORE extends Opcode {
    public CASTORE() {
        super(OpcodeType.ARRAY, Opcode.CASTORE, 1);
    }
}

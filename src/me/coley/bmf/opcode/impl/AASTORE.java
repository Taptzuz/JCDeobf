package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class AASTORE extends Opcode {
    public AASTORE() {
        super(OpcodeType.ARRAY, Opcode.AASTORE, 1);
    }
}

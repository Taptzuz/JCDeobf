package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class BASTORE extends Opcode {
    public BASTORE() {
        super(OpcodeType.ARRAY, Opcode.BASTORE, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DASTORE extends Opcode {
    public DASTORE() {
        super(OpcodeType.ARRAY, Opcode.DASTORE, 1);
    }
}

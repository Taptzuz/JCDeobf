package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IASTORE extends Opcode {
    public IASTORE() {
        super(OpcodeType.ARRAY, Opcode.IASTORE, 1);
    }
}

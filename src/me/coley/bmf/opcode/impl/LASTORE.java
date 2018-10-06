package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LASTORE extends Opcode {
    public LASTORE() {
        super(OpcodeType.ARRAY, Opcode.LASTORE, 1);
    }
}

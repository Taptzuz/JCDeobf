package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LALOAD extends Opcode {
    public LALOAD() {
        super(OpcodeType.ARRAY, Opcode.LALOAD, 1);
    }
}

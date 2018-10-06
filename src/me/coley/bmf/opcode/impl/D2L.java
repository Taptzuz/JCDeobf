package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class D2L extends Opcode {
    public D2L() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.D2L, 1);
    }
}

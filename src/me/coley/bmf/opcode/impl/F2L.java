package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class F2L extends Opcode {
    public F2L() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.F2L, 1);
    }
}

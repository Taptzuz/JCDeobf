package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class D2F extends Opcode {
    public D2F() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.D2F, 1);
    }
}

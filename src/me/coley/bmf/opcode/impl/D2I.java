package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class D2I extends Opcode {
    public D2I() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.D2I, 1);
    }
}

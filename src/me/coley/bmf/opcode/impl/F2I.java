package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class F2I extends Opcode {
    public F2I() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.F2I, 1);
    }
}

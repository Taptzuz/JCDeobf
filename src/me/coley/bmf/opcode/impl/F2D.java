package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class F2D extends Opcode {
    public F2D() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.F2D, 1);
    }
}

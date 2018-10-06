package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class L2D extends Opcode {
    public L2D() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.L2D, 1);
    }
}

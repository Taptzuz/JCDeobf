package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class L2F extends Opcode {
    public L2F() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.L2F, 1);
    }
}

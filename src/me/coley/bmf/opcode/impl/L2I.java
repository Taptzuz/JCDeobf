package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class L2I extends Opcode {
    public L2I() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.L2I, 1);
    }
}

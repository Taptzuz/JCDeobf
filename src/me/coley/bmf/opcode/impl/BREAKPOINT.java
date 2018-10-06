package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class BREAKPOINT extends Opcode {
    public BREAKPOINT() {
        super(OpcodeType.OTHER, Opcode.BREAKPOINT, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class MONITOREXIT extends Opcode {
    public MONITOREXIT() {
        super(OpcodeType.OTHER, Opcode.MONITOREXIT, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class AALOAD extends Opcode {
    public AALOAD() {
        super(OpcodeType.ARRAY, Opcode.AALOAD, 1);
    }
}

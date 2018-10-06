package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class ATHROW extends Opcode {
    public ATHROW() {
        super(OpcodeType.ARRAY, Opcode.ATHROW, 1);
    }
}

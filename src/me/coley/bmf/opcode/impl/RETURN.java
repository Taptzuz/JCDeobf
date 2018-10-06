package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class RETURN extends Opcode {
    public RETURN() {
        super(OpcodeType.RETURN, Opcode.RETURN, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class SWAP extends Opcode {
    public SWAP() {
        super(OpcodeType.STACK, Opcode.SWAP, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class DCMPL extends Opcode {
    public DCMPL() {
        super(OpcodeType.STACK, Opcode.DCMPL, 1);
    }
}

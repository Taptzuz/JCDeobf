package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class FCMPL extends Opcode {
    public FCMPL() {
        super(OpcodeType.STACK, Opcode.FCMPL, 1);
    }
}

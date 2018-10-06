package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class MONITORENTER extends Opcode {
    public MONITORENTER() {
        super(OpcodeType.OTHER, Opcode.MONITORENTER, 1);
    }
}

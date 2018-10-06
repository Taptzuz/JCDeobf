package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFLT extends AbstractJump {
    public IFLT(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFLT, jumpIndex);
    }
}

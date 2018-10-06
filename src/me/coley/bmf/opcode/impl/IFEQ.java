package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFEQ extends AbstractJump {
    public IFEQ(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFEQ, jumpIndex);
    }
}

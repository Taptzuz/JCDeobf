package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFLE extends AbstractJump {
    public IFLE(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFLE, jumpIndex);
    }
}

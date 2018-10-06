package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFGE extends AbstractJump {
    public IFGE(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFGE, jumpIndex);
    }
}

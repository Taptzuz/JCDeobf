package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IF_ACMPEQ extends AbstractJump {
    public IF_ACMPEQ(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IF_ACMPEQ, jumpIndex);
    }
}

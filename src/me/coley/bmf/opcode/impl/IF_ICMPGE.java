package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IF_ICMPGE extends AbstractJump {
    public IF_ICMPGE(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IF_ICMPGE, jumpIndex);
    }
}

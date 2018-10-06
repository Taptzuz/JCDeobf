package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IF_ICMPNE extends AbstractJump {
    public IF_ICMPNE(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IF_ICMPNE, jumpIndex);
    }
}

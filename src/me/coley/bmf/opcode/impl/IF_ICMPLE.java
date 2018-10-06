package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IF_ICMPLE extends AbstractJump {
    public IF_ICMPLE(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IF_ICMPLE, jumpIndex);
    }
}

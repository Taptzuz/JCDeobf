package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IF_ACMPNE extends AbstractJump {
    public IF_ACMPNE(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IF_ACMPNE, jumpIndex);
    }
}

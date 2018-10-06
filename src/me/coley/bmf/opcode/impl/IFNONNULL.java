package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFNONNULL extends AbstractJump {
    public IFNONNULL(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFNONNULL, jumpIndex);
    }
}

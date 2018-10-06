package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFNE extends AbstractJump {
    public IFNE(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFNE, jumpIndex);
    }
}

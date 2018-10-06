package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFGT extends AbstractJump {
    public IFGT(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFGT, jumpIndex);
    }
}

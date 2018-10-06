package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IFNULL extends AbstractJump {
    public IFNULL(int jumpIndex) {
        super(OpcodeType.ARRAY, Opcode.IFNULL, jumpIndex);
    }
}

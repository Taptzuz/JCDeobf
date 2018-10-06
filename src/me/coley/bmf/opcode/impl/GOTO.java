package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractJump;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class GOTO extends AbstractJump {
    public GOTO(int jumpIndex) {
        super(OpcodeType.FLOW_CONTROL, jumpIndex > Short.MAX_VALUE ? Opcode.GOTO_W : Opcode.GOTO, jumpIndex);
    }
}

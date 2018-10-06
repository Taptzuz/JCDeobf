package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;
import me.coley.bmf.opcode.SingleValueOpcode;

public class SIPUSH extends SingleValueOpcode<Integer> {
    public SIPUSH(int value) {
        super(OpcodeType.STACK, Opcode.SIPUSH, 3, value);
    }
}

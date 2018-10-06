package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;
import me.coley.bmf.opcode.SingleValueOpcode;

public class BIPUSH extends SingleValueOpcode<Integer> {
    public BIPUSH(int value) {
        super(OpcodeType.STACK, Opcode.BIPUSH, 2, value);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractMethodOpcode;
import me.coley.bmf.opcode.Opcode;

public class INVOKESTATIC extends AbstractMethodOpcode {
    public INVOKESTATIC(int methodIndex) {
        super(Opcode.INVOKESTATIC, 5, methodIndex);
    }
}

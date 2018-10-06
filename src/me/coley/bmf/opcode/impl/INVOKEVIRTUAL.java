package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractMethodOpcode;
import me.coley.bmf.opcode.Opcode;

public class INVOKEVIRTUAL extends AbstractMethodOpcode {
    public INVOKEVIRTUAL(int methodIndex) {
        super(Opcode.INVOKEVIRTUAL, 5, methodIndex);
    }
}

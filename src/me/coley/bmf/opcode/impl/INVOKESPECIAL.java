package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractMethodOpcode;
import me.coley.bmf.opcode.Opcode;

public class INVOKESPECIAL extends AbstractMethodOpcode {
    public INVOKESPECIAL(int methodIndex) {
        super(Opcode.INVOKESPECIAL, 5, methodIndex);
    }
}

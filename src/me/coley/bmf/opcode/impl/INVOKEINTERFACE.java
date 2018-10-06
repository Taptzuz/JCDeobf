package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractMethodOpcode;
import me.coley.bmf.opcode.Opcode;

public class INVOKEINTERFACE extends AbstractMethodOpcode {
    public int argWords;

    public INVOKEINTERFACE(int methodIndex, int argWords, int zero) {
        super(Opcode.INVOKEINTERFACE, 7, methodIndex);
        this.argWords = argWords;
    }
}

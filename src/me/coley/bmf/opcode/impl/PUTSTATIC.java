package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractFieldOpcode;
import me.coley.bmf.opcode.Opcode;

public class PUTSTATIC extends AbstractFieldOpcode {
    public PUTSTATIC( int fieldIndex) {
        super(Opcode.PUTSTATIC, fieldIndex);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractFieldOpcode;
import me.coley.bmf.opcode.Opcode;

public class GETSTATIC extends AbstractFieldOpcode {
    public GETSTATIC( int fieldIndex) {
        super(Opcode.GETSTATIC, fieldIndex);
    }
}

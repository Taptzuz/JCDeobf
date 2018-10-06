package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractFieldOpcode;
import me.coley.bmf.opcode.Opcode;

public class GETFIELD extends AbstractFieldOpcode {
    public GETFIELD( int fieldIndex) {
        super(Opcode.GETFIELD, fieldIndex);
    }
}

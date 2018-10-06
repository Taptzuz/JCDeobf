package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractFieldOpcode;
import me.coley.bmf.opcode.Opcode;

public class PUTFIELD extends AbstractFieldOpcode {
    public PUTFIELD( int fieldIndex) {
        super(Opcode.PUTFIELD, fieldIndex);
    }
}

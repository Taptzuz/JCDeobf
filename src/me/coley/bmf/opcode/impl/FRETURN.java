package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractRETURN;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;
import me.coley.bmf.type.Type;

public class FRETURN extends AbstractRETURN {
    public FRETURN() {
        super(OpcodeType.RETURN, Opcode.FRETURN, Type.FLOAT);
    }
}

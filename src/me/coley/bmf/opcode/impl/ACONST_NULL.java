package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class ACONST_NULL extends Opcode {
    public ACONST_NULL() {
        super(OpcodeType.CONST_VALUE, Opcode.ACONST_NULL, 1);
    }
}

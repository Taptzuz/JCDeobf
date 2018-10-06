package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;
import me.coley.bmf.opcode.SingleValueOpcode;

public class FCONST extends SingleValueOpcode<Float> {
    public FCONST(float value) {
        super(OpcodeType.CONST_VALUE, opFromValue((int) Math.round(value)), 1, value);
    }

    private static int opFromValue(int value) {
        if (value == 0)
            return Opcode.FCONST_0;
        if (value == 1)
            return Opcode.FCONST_1;
        else return Opcode.FCONST_2;
    }
}

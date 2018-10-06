package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;
import me.coley.bmf.opcode.SingleValueOpcode;

public class LCONST extends SingleValueOpcode<Long> {
    public LCONST(long value) {
        super(OpcodeType.CONST_VALUE, opFromValue((int) value), 1, value);
    }

    private static int opFromValue(int value) {
        if (value == 0)
            return Opcode.LCONST_0;
        else return Opcode.LCONST_1;
    }
}

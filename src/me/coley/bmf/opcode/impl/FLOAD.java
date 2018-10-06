package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractLOAD;
import me.coley.bmf.opcode.Opcode;

public class FLOAD extends AbstractLOAD {
    public FLOAD(int index) {
        super(opFromIndex(index), index);
    }

    private static int opFromIndex(int index) {
        if (index == 0)
            return Opcode.FLOAD_0;
        else if (index == 1)
            return Opcode.FLOAD_1;
        else if (index == 2)
            return Opcode.FLOAD_2;
        else if (index == 3)
            return Opcode.FLOAD_3;
        else return Opcode.FLOAD;
    }
}

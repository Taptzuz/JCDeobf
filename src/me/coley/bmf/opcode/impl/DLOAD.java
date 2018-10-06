package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractLOAD;
import me.coley.bmf.opcode.Opcode;

public class DLOAD extends AbstractLOAD {
    public DLOAD(int index) {
        super(opFromIndex(index), index);
    }

    private static int opFromIndex(int index) {
        if (index == 0)
            return Opcode.DLOAD_0;
        else if (index == 1)
            return Opcode.DLOAD_1;
        else if (index == 2)
            return Opcode.DLOAD_2;
        else if (index == 3)
            return Opcode.DLOAD_3;
        else return Opcode.DLOAD;
    }
}

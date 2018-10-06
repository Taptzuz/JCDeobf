package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractLOAD;
import me.coley.bmf.opcode.Opcode;

public class ALOAD extends AbstractLOAD {
    public ALOAD(int index) {
        super(opFromIndex(index), index);
    }

    private static int opFromIndex(int index) {
        if (index == 0)
            return Opcode.ALOAD_0;
        else if (index == 1)
            return Opcode.ALOAD_1;
        else if (index == 2)
            return Opcode.ALOAD_2;
        else if (index == 3)
            return Opcode.ALOAD_3;
        else return Opcode.ALOAD;
    }
}

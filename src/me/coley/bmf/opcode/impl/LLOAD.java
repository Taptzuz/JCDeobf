package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractLOAD;
import me.coley.bmf.opcode.Opcode;

public class LLOAD extends AbstractLOAD {
    public LLOAD(int index) {
        super(opFromIndex(index), index);
    }

    private static int opFromIndex(int index) {
        if (index == 0)
            return Opcode.LLOAD_0;
        else if (index == 1)
            return Opcode.LLOAD_1;
        else if (index == 2)
            return Opcode.LLOAD_2;
        else if (index == 3)
            return Opcode.LLOAD_3;
        else return Opcode.LLOAD;
    }
}

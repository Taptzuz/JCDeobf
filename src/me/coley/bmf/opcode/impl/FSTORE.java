package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractSTORE;
import me.coley.bmf.opcode.Opcode;

public class FSTORE extends AbstractSTORE {
    public FSTORE(int index) {
        super(opFromIndex(index), index);
    }

    private static int opFromIndex(int index) {
        if (index == 0)
            return Opcode.FSTORE_0;
        else if (index == 1)
            return Opcode.FSTORE_1;
        else if (index == 2)
            return Opcode.FSTORE_2;
        else if (index == 3)
            return Opcode.FSTORE_3;
        else return Opcode.FSTORE;
    }
}

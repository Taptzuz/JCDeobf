package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractSTORE;
import me.coley.bmf.opcode.Opcode;

public class DSTORE extends AbstractSTORE {
    public DSTORE(int index) {
        super(opFromIndex(index), index);
    }

    private static int opFromIndex(int index) {
        if (index == 0)
            return Opcode.DSTORE_0;
        else if (index == 1)
            return Opcode.DSTORE_1;
        else if (index == 2)
            return Opcode.DSTORE_2;
        else if (index == 3)
            return Opcode.DSTORE_3;
        else return Opcode.DSTORE;
    }
}

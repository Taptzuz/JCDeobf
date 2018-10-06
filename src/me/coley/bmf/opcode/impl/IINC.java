package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class IINC extends Opcode {
    public int index, increment;

    public IINC(int index, int increment) {
        super(OpcodeType.VARIABLE, Opcode.IINC, 3);
        this.index = index;
        this.increment = increment;
    }
}

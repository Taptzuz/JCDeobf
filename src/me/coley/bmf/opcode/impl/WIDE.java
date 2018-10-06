package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class WIDE extends Opcode {
    /**
     * Special value used when WIDE 
     */
    public Integer n;
    public WIDE( int opcode, int length, Integer n) {
        super(OpcodeType.OTHER, Opcode.WIDE, n == null ? 4 : 6);
        this.n = n;
    }

}

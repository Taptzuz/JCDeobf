package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class NEWARRAY extends Opcode {
    //@formatter:off
    public static final int TYPE_BOOLEAN = 4;
    public static final int TYPE_CHAR    = 5;
    public static final int TYPE_FLOAT   = 6;
    public static final int TYPE_DOUBLE  = 7;
    public static final int TYPE_BYTE    = 8;
    public static final int TYPE_SHORT   = 9;
    public static final int TYPE_INT     = 10;
    public static final int TYPE_LONG    = 11;
    //@formatter:on
    public int type;

    public NEWARRAY(int type) {
        super(OpcodeType.ARRAY, Opcode.NEWARRAY, 2);
        this.type = type;
    }
}

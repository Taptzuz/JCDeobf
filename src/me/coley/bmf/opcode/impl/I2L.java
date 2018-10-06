package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class I2L extends Opcode {
    public I2L() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.I2L, 1);
    }
}

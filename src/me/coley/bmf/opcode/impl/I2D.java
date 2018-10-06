package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class I2D extends Opcode {
    public I2D() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.I2D, 1);
    }
}

package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class I2C extends Opcode {
    public I2C() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.I2C, 1);
    }
}

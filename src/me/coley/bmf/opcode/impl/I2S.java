package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class I2S extends Opcode {
    public I2S() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.I2S, 1);
    }
}

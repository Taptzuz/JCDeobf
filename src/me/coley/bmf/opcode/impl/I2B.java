package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class I2B extends Opcode {
    public I2B() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.I2B, 1);
    }
}

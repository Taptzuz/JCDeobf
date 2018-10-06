package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class I2F extends Opcode {
    public I2F() {
        super(OpcodeType.TYPE_CONVERSION, Opcode.I2F, 1);
    }
}

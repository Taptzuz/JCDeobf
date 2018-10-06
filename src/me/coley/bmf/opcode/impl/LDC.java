package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractLDC;
import me.coley.bmf.opcode.Opcode;

public class LDC extends AbstractLDC {
    public LDC(int index) {
        super(Opcode.LDC, 2, index);
    }
}

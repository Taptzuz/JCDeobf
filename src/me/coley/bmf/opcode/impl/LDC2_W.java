package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractLDC;
import me.coley.bmf.opcode.Opcode;

public class LDC2_W extends AbstractLDC {
    public LDC2_W(int index) {
        // TODO: Check is size 3 is correct
        super(Opcode.LDC_W, 3, index);
    }
}

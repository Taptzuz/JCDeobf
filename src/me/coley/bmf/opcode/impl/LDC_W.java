package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.AbstractLDC;
import me.coley.bmf.opcode.Opcode;

public class LDC_W extends AbstractLDC {
    public LDC_W(int index) {
        super(Opcode.LDC_W, 3, index);
    }
}

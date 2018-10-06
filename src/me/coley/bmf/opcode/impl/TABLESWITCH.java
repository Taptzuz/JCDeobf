package me.coley.bmf.opcode.impl;

import java.util.List;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class TABLESWITCH extends Opcode {
    public int default_offset;
    public int low;
    public int high;
    public List<Integer> offsets;

    public TABLESWITCH(int default_offset, int low, int high, List<Integer> offsets) {
        super(OpcodeType.FLOW_CONTROL, Opcode.TABLESWITCH, 1);
        this.default_offset = default_offset;
        this.low = low;
        this.high = high;
        this.offsets = offsets;
    }

    @Override
    public int getLength() {
        // Padding not included. 
        // opcode + padding? + default_off + low + high + ... (offsets size:4)
        return 1 + 12 + (4 * (offsets.size()));
    }
}

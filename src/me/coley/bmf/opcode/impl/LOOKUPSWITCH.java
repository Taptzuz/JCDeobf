package me.coley.bmf.opcode.impl;

import java.util.List;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class LOOKUPSWITCH extends Opcode {
    public int default_offset;
    public List<OffsetPair> offsets;

    public LOOKUPSWITCH(int default_offset, List<OffsetPair> offsets) {
        super(OpcodeType.FLOW_CONTROL, Opcode.LOOKUPSWITCH, 9);
        this.default_offset = default_offset;
        this.offsets = offsets;
    }

    @Override
    public int getLength() {
        // Padding not included. 
        // opcode + padding? + default_off + n + ... (key, offset: size 4 each)
        return 1 + 8 + (8 * offsets.size()); 
    }

    public static class OffsetPair {
        public int key;
        public int offset;

        public OffsetPair(int key, int offset) {
            this.key = key;
            this.offset = offset;
        }
    }
}

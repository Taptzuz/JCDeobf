package me.coley.bmf.opcode;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class AbstractFieldOpcode extends Opcode {
    public int fieldIndex;

    public AbstractFieldOpcode(int opcode, int fieldIndex) {
        super(OpcodeType.STACK, opcode, 3);
        this.fieldIndex = fieldIndex;
    }

}

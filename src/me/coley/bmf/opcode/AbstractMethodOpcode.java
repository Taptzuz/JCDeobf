package me.coley.bmf.opcode;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class AbstractMethodOpcode extends Opcode {
    public int methodIndex;

    public AbstractMethodOpcode(int opcode, int size, int methodIndex) {
        super(OpcodeType.STACK, opcode, size);
        this.methodIndex = methodIndex;
    }

}

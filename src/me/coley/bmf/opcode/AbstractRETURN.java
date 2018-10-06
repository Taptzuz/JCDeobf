package me.coley.bmf.opcode;

import me.coley.bmf.type.Type;

public class AbstractRETURN extends Opcode {
    public final Type returnType;

    public AbstractRETURN(OpcodeType optype, int opcode, Type returnType) {
        super(optype, opcode, 1);
        this.returnType = returnType;
    }
}

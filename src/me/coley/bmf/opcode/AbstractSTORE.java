package me.coley.bmf.opcode;

public class AbstractSTORE extends SingleValueOpcode<Integer> {
    public AbstractSTORE(int opcode, int index) {
        super(OpcodeType.VARIABLE, opcode, 2, index);
    }
}

package me.coley.bmf.opcode;

public class AbstractLOAD extends SingleValueOpcode<Integer> {
    public AbstractLOAD(int opcode, int index) {
        super(OpcodeType.VARIABLE, opcode, 2, index);
    }
}

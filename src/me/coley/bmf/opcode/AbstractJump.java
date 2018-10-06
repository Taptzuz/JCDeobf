package me.coley.bmf.opcode;

public class AbstractJump extends Opcode {
    // TODO: Figure out a clean way to automate updating this whenever somebody
    // messes with the opcodes of the method.
    public int jumpIndex;

    public AbstractJump(OpcodeType optype, int opcode, int jumpIndex) {
        super(optype, opcode, 1);
        this.jumpIndex = jumpIndex;
    }
}

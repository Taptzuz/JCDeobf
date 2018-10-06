package me.coley.bmf.opcode;

/**
 * Opcode that points to a class index. This should be named better eventually.
 * 
 * @author Matt
 */
public class AbstractClassPointer extends Opcode {
    public int classIndex;

    public AbstractClassPointer(OpcodeType type, int opcode, int length, int classIndex) {
        super(type, opcode, 1);
        this.classIndex = classIndex;
    }
}

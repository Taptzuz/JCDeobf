package me.coley.bmf.opcode.impl;

import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeType;

public class INVOKEDYNAMIC extends Opcode {
    public int declaringClass;
    public int indyIndex;

    public INVOKEDYNAMIC(int declaringClass) {
        super(OpcodeType.STACK, Opcode.INVOKEDYNAMIC, 5);
        this.declaringClass = declaringClass;
    }

    // For writing:
    // byte - opcode
    // short - declaringClass
    // byte - 0
    // byte - 0

    // From javap, pretty sure InvokeDynamic index is based off their place in
    // the class file.
    // First instance = first Indy
    // Last instance = last Indy
}

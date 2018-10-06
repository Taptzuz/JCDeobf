package me.coley.bmf.signature;

import java.util.List;

public class TypeSignature extends Signature {
    private final List<SigArg> interfaces;

    public TypeSignature(SigArg type) {
        this(type, null, null);
    }

    public TypeSignature(SigArg type, TypeArgHelper helper, List<SigArg> interfaces) {
        this.helper = helper;
        this.interfaces = interfaces;
        this.type = type;
    }

    @Override
    public String toSignature() {
        // Build type parameters
        StringBuilder strVariables = new StringBuilder();
        if (helper != null) {
            strVariables.append(helper.toGeneric());
        }
        // List interfaces
        StringBuilder strInterfaces = new StringBuilder();
        if (interfaces != null && interfaces.size() > 0) {
            for (SigArg arg : interfaces) {
                strInterfaces.append(arg.toArg());
            }
        }
        // Combine
        return strVariables.toString() + type.toArg() + strInterfaces.toString();
    }

}

package me.coley.bmf.signature;

import java.util.List;

public class MethodSignature extends Signature {
    private List<SigArg> parameters;

    public MethodSignature(TypeArgHelper helper, List<SigArg> parameters, SigArg retType) {
        this.helper = helper;
        this.parameters = parameters;
        this.type = retType;
    }

    public List<SigArg> getMethodParams() {
        return parameters;
    }

    @Override
    public String toSignature() {
        // Build type parameters
        StringBuilder strVariables = new StringBuilder();
        if (helper != null) {
            strVariables.append(helper.toGeneric());
        }
        // List args
        StringBuilder strArgs = new StringBuilder();
        if (parameters != null && parameters.size() > 0) {
            for (SigArg arg : parameters) {
                strArgs.append(arg.toArg());
            }
        }
        // Combine
        return strVariables.toString() + "(" + strArgs.toString() + ")" + type.toArg();
    }
}

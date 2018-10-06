package me.coley.bmf.type.descriptors;

import java.util.List;

import me.coley.bmf.type.Type;

public class MethodDescriptor extends MemberDescriptor {

    public final List<Type> parameters;
    public Type returnType;

    public MethodDescriptor(String desc, List<Type> parameters, Type returnType) {
        super(desc);
        this.parameters = parameters;
        this.returnType = returnType;
    }

    @Override
    public String toDesc() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Type parameter : parameters) {
            sb.append(parameter.toDesc());
        }
        sb.append(")");
        sb.append(returnType.toDesc());
        return sb.toString();
    }

}

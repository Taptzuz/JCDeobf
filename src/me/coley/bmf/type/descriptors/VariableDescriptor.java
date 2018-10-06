package me.coley.bmf.type.descriptors;

import me.coley.bmf.type.Type;

public class VariableDescriptor extends MemberDescriptor {
    public Type type;

    public VariableDescriptor(Type type) {
        super(type.toDesc());
        this.type = type;
    }

    @Override
    public String toDesc() {
        return type.toDesc();
    }
}
package me.coley.bmf.mapping;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.type.descriptors.MemberDescriptor;
import me.coley.bmf.util.Box;

public class MethodMapping extends MemberMapping {
    private final List<MemberMapping> variables = new ArrayList<MemberMapping>();

    public MethodMapping(Box<String> name, MemberDescriptor desc) {
        super(name, desc);
    }

    public void addVariable(Mapping mapping, MemberMapping mm) {
        variables.add(mm);
    }

    public List<MemberMapping> getVariables() {
        return variables;
    }

}

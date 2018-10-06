package me.coley.bmf.consts.mapping;

import me.coley.bmf.consts.ConstUTF8;
import me.coley.bmf.type.descriptors.MemberDescriptor;

public class ConstMemberDesc extends ConstUTF8 {
    public final MemberDescriptor desc;

    public ConstMemberDesc(MemberDescriptor desc) {
        super();
        // For testing.
        // TODO: Decide if this practice should be applied globally
        if (desc == null)
            throw new RuntimeException("Cannot have a null value for ConstMemberDesc!");
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return desc.toDesc();
    }
}

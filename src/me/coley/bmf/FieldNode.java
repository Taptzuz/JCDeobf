package me.coley.bmf;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.field.AttributeConstantValue;

/**
 * A field.
 *
 * @author Matt
 */
public class FieldNode extends MemberNode {
    /**
     * Attribute: Finalized value of the field <i>(Will be null if field is not
     * final, or the value is not constant)</i>
     */
    public AttributeConstantValue value;

    public FieldNode(ClassNode owner) {
        super(owner);
    }

    @Override
    public void addAttribute(Attribute attribute) {
        switch (attribute.type) {
        case CONSTANT_VALUE:
            value = (AttributeConstantValue) attribute;
            break;
        default:
            super.addAttribute(attribute);
        }
    }

    @Override
    public List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList<>();
        if (value != null)
            attributes.add(value);
        if (signature != null)
            attributes.add(signature);
        if (runtimeInvisibleAnnotations != null)
            attributes.add(runtimeInvisibleAnnotations);
        if (runtimeVisibleAnnotations != null)
            attributes.add(runtimeVisibleAnnotations);
        if (runtimeVisibleParamAnnotations != null)
            attributes.add(runtimeVisibleParamAnnotations);
        if (runtimeInvisibleParamAnnotations != null)
            attributes.add(runtimeInvisibleParamAnnotations);
        if (deprecated != null)
            attributes.add(deprecated);
        if (synthetic != null)
            attributes.add(synthetic);
        return attributes;
    }
}

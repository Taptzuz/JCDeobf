package me.coley.bmf.attribute.field;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;

public class AttributeConstantValue extends Attribute {
    public int constantIndex;

    public AttributeConstantValue(int name, int constantIndex) {
        super(name, AttributeType.CONSTANT_VALUE);
        this.constantIndex = constantIndex;
    }

    @Override
    public int getLength() {
        // u2: constantIndex
        return 2;
    }
}

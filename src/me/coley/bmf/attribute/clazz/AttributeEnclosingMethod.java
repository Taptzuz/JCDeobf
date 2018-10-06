package me.coley.bmf.attribute.clazz;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;

public class AttributeEnclosingMethod extends Attribute {
    public int classIndex;
    public int methodIndex;

    public AttributeEnclosingMethod(int name, int classIndex, int methodIndex) {
        super(name, AttributeType.ENCLOSING_METHOD);
        this.classIndex = classIndex;
        this.methodIndex = methodIndex;
    }

    @Override
    public int getLength() {
        // u2: class_index
        // u2: method_index
        return 4;
    }
}

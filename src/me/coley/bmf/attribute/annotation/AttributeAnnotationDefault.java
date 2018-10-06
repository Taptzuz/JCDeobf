package me.coley.bmf.attribute.annotation;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.attribute.annotation.element.ElementValue;

public class AttributeAnnotationDefault extends Attribute {
    public ElementValue value;

    public AttributeAnnotationDefault(int name, ElementValue value) {
        super(name, AttributeType.ANNOTATION_DEFAULT);
        this.value = value;
    }

    @Override
    public int getLength() {
        // element_value: value
        return value.getLength();
    }
}

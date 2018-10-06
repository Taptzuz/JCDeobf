package me.coley.bmf.attribute.clazz;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.util.MeasurableUtils;

public class AttributeInnerClasses extends Attribute {
    public List<InnerClass> classes;

    public AttributeInnerClasses(int name, List<InnerClass> classes) {
        super(name, AttributeType.INNER_CLASSES);
        this.classes = classes;
    }

    @Override
    public int getLength() {
        // u2: num_classes
        // ?: classes
        return 2 + MeasurableUtils.getLength(classes);
    }

}

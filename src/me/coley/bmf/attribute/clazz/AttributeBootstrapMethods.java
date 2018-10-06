package me.coley.bmf.attribute.clazz;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.util.MeasurableUtils;

public class AttributeBootstrapMethods extends Attribute {
    public List<BootstrapMethod> methods;

    public AttributeBootstrapMethods(int name, List<BootstrapMethod> methods) {
        super(name, AttributeType.BOOTSTRAP_METHODS);
        this.methods = methods;
    }

    @Override
    public int getLength() {
        // u2: num_methods
        // ??:
        return 2 + MeasurableUtils.getLength(methods);
    }
}

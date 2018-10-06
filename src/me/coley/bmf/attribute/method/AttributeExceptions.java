package me.coley.bmf.attribute.method;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;

public class AttributeExceptions extends Attribute {
    public List<Integer> exceptionIndicies;

    public AttributeExceptions(int name, List<Integer> exceptionIndicies) {
        super(name, AttributeType.EXCEPTIONS);
        this.exceptionIndicies = exceptionIndicies;
    }

    @Override
    public int getLength() {
        // u2: num_exceptions
        // u2[]: exceptions
        return 2 + (2 * exceptionIndicies.size());
    }
}

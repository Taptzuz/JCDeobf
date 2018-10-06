package me.coley.bmf.attribute.clazz;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;

public class AttributeSourceDebugExtension extends Attribute {
    /**
     * Holds extended debugging information which has no semantic effect on the
     * Java Virtual Machine. The information is represented using a modified
     * UTF-8 string with no terminating zero byte.
     */
    public List<Integer> data;

    public AttributeSourceDebugExtension(int name, List<Integer> data) {
        super(name, AttributeType.SOURCE_DEBUG_EXTENSION);
        this.data = data;
    }

    @Override
    public int getLength() {
        // u1[]: debug_extension[attribute_length]
        // TODO Verify that this is correct
        return data.size();
    }
}

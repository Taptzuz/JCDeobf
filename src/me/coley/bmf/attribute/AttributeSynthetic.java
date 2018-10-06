package me.coley.bmf.attribute;

public class AttributeSynthetic extends Attribute {

    public AttributeSynthetic(int name) {
        super(name, AttributeType.SYNTHETIC);
    }

    @Override
    public int getLength() {
        return 0;
    }
}

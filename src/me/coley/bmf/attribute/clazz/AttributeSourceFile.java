package me.coley.bmf.attribute.clazz;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;

public class AttributeSourceFile extends Attribute {
    public int sourceFile;

    public AttributeSourceFile(int name, int sourceFile) {
        super(name, AttributeType.SOURCE_FILE);
        this.sourceFile = sourceFile;
    }

    @Override
    public int getLength() {
        // u2: source_index
        return 2;
    }
}

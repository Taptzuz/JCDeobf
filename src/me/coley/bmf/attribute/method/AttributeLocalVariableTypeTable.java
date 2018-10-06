package me.coley.bmf.attribute.method;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.util.MeasurableUtils;

public class AttributeLocalVariableTypeTable extends Attribute {
    public List<LocalVariableType> localTypes;

    public AttributeLocalVariableTypeTable(int name, List<LocalVariableType> localTypes) {
        super(name, AttributeType.LOCAL_VARIABLE_TYPE_TABLE);
        this.localTypes = localTypes;
    }

    @Override
    public int getLength() {
        // u2: num_variables
        // ??: variable_table
        return 2 + MeasurableUtils.getLength(localTypes);
    }
}

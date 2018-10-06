package me.coley.bmf.attribute.method;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;

/**
 * An attribute belonging to the
 * "{@link me.coley.bmf.attribute.method.AttributeCode Code}" attribute of a
 * method. Contains information about local variables.
 *
 * @author Matt
 */
public class AttributeLocalVariableTable extends Attribute {
    /**
     * The {@link me.coley.bmf.attribute.method.LocalVariableTable table}.
     */
    public LocalVariableTable variableTable;

    public AttributeLocalVariableTable(int name, LocalVariableTable variableTable) {
        super(name, AttributeType.LOCAL_VARIABLE_TABLE);
        this.variableTable = variableTable;
    }

    @Override
    public int getLength() {
        return variableTable.getLength();
    }
}

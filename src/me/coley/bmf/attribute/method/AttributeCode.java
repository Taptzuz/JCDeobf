package me.coley.bmf.attribute.method;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.util.MeasurableUtils;

/**
 * The Code of a method. Contains sub-attributes:
 * <ul>
 * <li>{@link #lines LineNumberTable} - <i>May be null</i>
 * <li>{@link #variables LocalVariableTable} - <i>May be null</i>
 * <li>{@link #variableTypes LocalVariableTypeTable}
 * <li>{@link #stackMap StackMapTable}
 * </ul>
 * May contain others, but those four are standard.
 *
 * @author Matt
 */
public class AttributeCode extends Attribute {
    /**
     * Max number of values allowed on the stack.
     */
    public int maxStack;
    /**
     * Max number of variables allowed in the method.
     */
    public int maxLocals;

    /**
     * A list of {@link me.coley.bmf.attribute.method.MethodException
     * exceptions}.
     */
    public List<MethodException> exceptions;

    /**
     * A {@link me.coley.bmf.attribute.method.AttributeLineNumberTable table}
     * that correlates opcode indices and line numbers <i>(Debugging). <br>
     * May be null.</i>
     */
    public AttributeLineNumberTable lines;
    /**
     * A {@link me.coley.bmf.attribute.method.AttributeLocalVariableTable
     * table} containing local variables.
     */
    public AttributeLocalVariableTable variables;
    /**
     * A {@link me.coley.bmf.attribute.method.AttributeLocalVariableTypeTable
     * table} containing types of local variables. <br>
     * <i>May be null</i>
     */
    public AttributeLocalVariableTypeTable variableTypes;
    /**
     * A {@link me.coley.bmf.attribute.method.AttributeStackMapTable table}
     * that supplies information about the stack and stored locals at given
     * bytecode offsets.
     */
    public AttributeStackMapTable stackMap;

    /**
     * A {@link me.coley.bmf.attribute.method.MethodException structure}
     * containing the opcodes.
     */
    public MethodCode opcodes;

    public AttributeCode(int name, int stack, int locals, List<MethodException> exceptions, MethodCode opcodes,
            List<Attribute> attributes) {
        super(name, AttributeType.CODE);
        this.maxStack = stack;
        this.maxLocals = locals;
        this.exceptions = exceptions;
        this.opcodes = opcodes;
        for (Attribute attribute : attributes) {
            switch (attribute.type) {
            case LINE_NUMBER_TABLE:
                lines = (AttributeLineNumberTable) attribute;
                break;
            case LOCAL_VARIABLE_TABLE:
                variables = (AttributeLocalVariableTable) attribute;
                break;
            case LOCAL_VARIABLE_TYPE_TABLE:
                variableTypes = (AttributeLocalVariableTypeTable) attribute;
                break;
            case STACK_MAP_TABLE:
                stackMap = (AttributeStackMapTable) attribute;
                break;
            default:
                System.err.println("UNHANDLED CODE ATTRIB: " + attribute.type.name());
                break;
            }
        }
    }

    @Override
    public int getLength() {
        // Common to all attributes, only needed in length calculation for
        // embedded attributes.
        // u2: name_index
        // u4: attribute_length
        int baseLen = 6;
        // u2: max_stack
        // u2: max_locals
        int len = 4;
        // u4: code_length
        // ??: CODE
        len += 4;
        if (opcodes != null) {
            len += opcodes.getLength();
        }
        // u2: exception_table_length
        // ??: EXCEPTIONS
        len += 2;
        if (exceptions.size() > 0) {
            len += MeasurableUtils.getLength(exceptions);
        }
        // u2: attributes_count
        // ??: ATTRIBS
        len += 2;
        if (lines != null) {
            len += baseLen + lines.getLength();
        }
        if (variables != null) {
            len += baseLen + variables.getLength();
        }
        if (variableTypes != null) {
            len += baseLen + variableTypes.getLength();
        }
        if (stackMap != null) {
            len += baseLen + stackMap.getLength();
        }
        return len;
    }

}

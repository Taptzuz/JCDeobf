package me.coley.bmf.opcode;

import me.coley.bmf.ClassNode;
import me.coley.bmf.consts.ConstString;
import me.coley.bmf.consts.ConstUTF8;
import me.coley.bmf.consts.ConstantType;

public abstract class AbstractLDC extends SingleValueOpcode<Integer> {
    public AbstractLDC(int opcode, int size, int index) {
        super(OpcodeType.CONST_POOL, opcode, size, index);
    }

    /**
     * Get the type of constant the LDC refers to in the given ClassNode's
     * constant-pool.
     * 
     * @param node
     * @return Type of constant
     */
    public ConstantType getValueType(ClassNode node) {
        return node.getConst(value).type;
    }

    /**
     * Get the referenced value.
     * 
     * @param node
     * @return Referenced value
     */
    public Object getValue(ClassNode node) {
        return node.getConst(value).getValue();
    }

    /**
     * Used if opcode is LDC or LDC_W
     * 
     * @param node
     * @return Referenced value as int
     */
    public int getInt(ClassNode node) {
        return (int) getValue(node);
    }

    /**
     * Used if opcode is LDC or LDC_W
     * 
     * @param node
     * @return Referenced value as float
     */
    public float getFloat(ClassNode node) {
        return (float) getValue(node);
    }

    /**
     * Used if opcode is LDC or LDC_W
     * 
     * @param node
     * @return Referenced value as String
     */
    public String getString(ClassNode node) {
        ConstString cs = (ConstString) node.getConst(value);
        ConstUTF8 utf = (ConstUTF8) node.getConst(cs.getValue());
        return utf.getValue();
    }

    /**
     * Used only if opcode is LDC2_W
     * 
     * @param node
     * @return Referenced value as double
     */
    public double getDouble(ClassNode node) {
        return (double) getValue(node);
    }

    /**
     * Used only if opcode is LDC2_W
     * 
     * @param node
     * @return Referenced value as long
     */
    public long getLong(ClassNode node) {
        return (long) getValue(node);
    }
}

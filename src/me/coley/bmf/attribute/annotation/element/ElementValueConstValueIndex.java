package me.coley.bmf.attribute.annotation.element;

public class ElementValueConstValueIndex extends ElementValue {
    /**
     * Points to a primitive constant value in the constant pool.
     */
    public int constValueIndex;

    public ElementValueConstValueIndex(ElementValueType type, int constValueIndex) {
        super(type);
        this.constValueIndex = constValueIndex;
    }

    @Override
    public int getLength() {
        // u2: const_value_index
        return BASE_LEN + 2;
    }
}

package me.coley.bmf.attribute.annotation.element;

public class ElementValueEnumConstValue extends ElementValue {
    /**
     * Points to a UTF8 constant pool value indicating a field descriptor.
     */
    public int type;
    /**
     * Points to a UTF constant pool value indicating the name of the enum
     * constant.
     */
    public int name;

    public ElementValueEnumConstValue(int type, int name) {
        super(ElementValueType.ENUM_CONST_VALUE);
        this.type = type;
        this.name = name;
    }

    @Override
    public int getLength() {
        // u2: type_name_index
        // u2: const_name_index
        return BASE_LEN + 4;
    }
}

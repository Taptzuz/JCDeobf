package me.coley.bmf.attribute.annotation.element;

import java.util.HashMap;
import java.util.Map;

public enum ElementValueType {
    //@formatter:off
    CONST_VALUE_INDEX_BYTE('B'),
    CONST_VALUE_INDEX_C('C'),
    CONST_VALUE_INDEX_DOUBLE('D'),
    CONST_VALUE_INDEX_FLOAT('F'),
    CONST_VALUE_INDEX_INT('I'),
    CONST_VALUE_INDEX_LONG('J'),
    CONST_VALUE_INDEX_SHORT('S'),
    CONST_VALUE_INDEX_BOOLEAN('Z'),
    CONST_VALUE_INDEX_s('s'),
    ENUM_CONST_VALUE('e'),
    CLASS_INFO_INDEX('c'),
    ANNOTATION_VALUE('@'),
    ARRAY_VALUE('[');
    //@formatter:on

    private static Map<Character, ElementValueType> typeMap;
    public final char key;

    ElementValueType(char key) {
        this.key = key;
        register(this);
    }

    /**
     * Retrieves an ElementValueType by a given name.
     *
     * @param tag
     * @return
     */
    public static ElementValueType fromType(char tag) {
        return typeMap.get(tag);
    }

    /**
     * Registers the ElementValueType's associated types to an instance.
     *
     * @param valueType
     */
    private void register(ElementValueType type) {
        if (typeMap == null) {
            typeMap = new HashMap<>();
        }
        typeMap.put(key, type);
    }
}

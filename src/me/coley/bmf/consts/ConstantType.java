package me.coley.bmf.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration for handling constant pool entry types.
 *
 * @author Matt
 */
public enum ConstantType {
    //@formatter:off
	UTF8(1),
    INT(3),
    FLOAT(4),
    LONG(5),
    DOUBLE(6),
    CLASS(7),
    STRING(8),
    FIELD(9),
    METHOD(10),
    INTERFACE_METHOD(11),
    NAME_TYPE(12),
    METHOD_HANDLE(15),
    METHOD_TYPE(16),
    INVOKEDYNAMIC(18);
    //@formatter:on

    /**
     * The tag of the Constant type.
     */
    private static Map<Integer, ConstantType> typeMap;
    private final int tag;

    ConstantType(int tag) {
        this.tag = tag;
        register(this);
    }

    /**
     * Retrieves a ConstantType instance from a given tag.
     *
     * @param tag
     * @return
     */
    public static ConstantType fromTag(int tag) {
        return typeMap.get(tag);
    }

    /**
     * Checks if given tag matches a const type.
     *
     * @param tag
     * @param type
     * @return
     */
    public static boolean matches(int tag, ConstantType type) {
        return (tag & type.tag) != 0;
    }

    /**
     * Registers the ConstantType's tag to an instance.
     *
     * @param type
     */
    private void register(ConstantType type) {
        if (typeMap == null) {
            typeMap = new HashMap<>();
        }
        typeMap.put(tag, type);
    }

    public int getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return name().replace("_", " ").toLowerCase();
    }
}

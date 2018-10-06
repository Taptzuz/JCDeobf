package me.coley.bmf.attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration for handling attributes.
 *
 * @author Matt
 */
public enum AttributeType {
    //@formatter:off
    CONSTANT_VALUE("ConstantValue"),
    CODE("Code"),
    // Something really odd is occurring
    // Some classes call it a StackMap
    // Others call it a StackMapTable
    //
    //STACK_MAP_TABLE("StackMap"),
    STACK_MAP_TABLE("StackMapTable"),
    EXCEPTIONS("Exceptions"),
    INNER_CLASSES("InnerClasses"),
    ENCLOSING_METHOD("EnclosingMethod"),
    SYNTHETIC("Synthetic"),
    SIGNATURE("Signature"),
    SOURCE_FILE("SourceFile"),
    SOURCE_DEBUG_EXTENSION("SourceDebugExtension"),
    LINE_NUMBER_TABLE("LineNumberTable"),
    LOCAL_VARIABLE_TABLE("LocalVariableTable"),
    LOCAL_VARIABLE_TYPE_TABLE("LocalVariableTypeTable"),
    DEPRECATED("Deprecated"),
    RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations"),
    RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations"),
    RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS("RuntimeVisibleParameterAnnotations"),
    RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS("RuntimeInvisibleParameterAnnotations"),
    ANNOTATION_DEFAULT("AnnotationDefault"),
    BOOTSTRAP_METHODS("BootstrapMethods");
    //@formatter:ON
    private static Map<String, AttributeType> nameMap;
    private final String name;

    AttributeType(String name) {
        this.name = name;
        register(this);
    }

    /**
     * Retrieves an Attribute instance by a given name.
     *
     * @param name
     * @return
     */
    public static AttributeType fromName(String name) {
        return nameMap.get(name);
    }

    /**
     * Registers the Attribute's name to an instance.
     *
     * @param attribute
     */
    private void register(AttributeType attribute) {
        if (nameMap == null) {
            nameMap = new HashMap<>();
        }
        nameMap.put(name, attribute);
    }
}

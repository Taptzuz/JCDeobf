package me.coley.bmf.attribute;

import me.coley.bmf.util.Measurable;

/**
 * Generic Attribute.
 *
 * @author Matt
 */
public abstract class Attribute implements Measurable {
    /**
     * An attribute's {@link me.coley.bmf.attribute.AttributeType type}.
     */
    public final AttributeType type;
    /**
     * Index of the attribute's name in the constant pool.
     */
    public int name;

    public Attribute(int name, AttributeType type) {
        this.type = type;
        this.name = name;
    }
}

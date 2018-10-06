package me.coley.bmf.attribute.annotation.element;

import me.coley.bmf.util.Measurable;

/**
 * A structure used by annotations that represent different values depending on
 * its type.
 *
 * @author Matt
 */
public abstract class ElementValue implements Measurable {
    /**
     * <pre>
     * u1: tag
     * </pre>
     */
    protected final static int BASE_LEN = 1;
    public ElementValueType type;

    public ElementValue(ElementValueType type) {
        this.type = type;
    }
}

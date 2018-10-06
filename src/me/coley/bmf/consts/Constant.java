package me.coley.bmf.consts;

/**
 * A generic constant class. An entry in a class's constant pool.
 *
 * @param <T>
 * @author Matt
 */
public abstract class Constant<T> {
    /**
     * Constant type.
     */
    public final ConstantType type;
    /**
     * Constant's value.
     */
    private T value;

    public Constant(ConstantType type) {
        this(type, null);
    }

    public Constant(ConstantType type, T value) {
        this.type = type;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return type.toString() + ":" + getValue();
    }
}

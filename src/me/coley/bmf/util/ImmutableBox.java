package me.coley.bmf.util;

public class ImmutableBox<T> extends Box<T> {

    public ImmutableBox(T value) {
        super(value);
    }

    @Override
    public void setValue(T value) {
        // Should an exception be thrown as feedback?
    }
}

package me.coley.bmf.util.io;

/**
 * Simple filter. Implementations should check for valid input, and optionally
 * implement renaming of matched input.
 */
public interface NameFilter {
    /**
     * Determines if a given name is valid.
     *
     * @param name
     * @return
     */
    public boolean matches(String name);

    /**
     * Returns a modified name.
     *
     * @param name
     * @return
     */
    public default String filterName(String name) {
        return name;
    }
}

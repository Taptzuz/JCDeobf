package me.coley.bmf.attribute.method;

import me.coley.bmf.util.Measurable;

/**
 * Local variable entry for the
 * {@link me.coley.bmf.attribute.method.LocalVariableTable}.
 * <ul>
 * <li>{@link #start}
 * <li>{@link #length}
 * <li>{@link #name}
 * <li>{@link #desc}
 * <li>{@link #index}
 * </ul>
 *
 * @author Matt
 */
public class Local implements Measurable {

    /**
     * Start index in the opcodes
     */
    public int start;
    /**
     * Length from start local lasts in the opcodes
     */
    public int length;
    /**
     * Constant pool pointers
     */
    public int name;
    public int desc;
    /**
     * Stack frame index. Doubles/Longs take two indices.
     */
    public int index;

    /**
     * Creates and assigned values to the local variable entry.
     *
     * @param start
     *            {@link #start}
     * @param length
     *            {@link #length}
     * @param name
     *            {@link #name}
     * @param desc
     *            {@link #desc}
     * @param index
     *            {@link #index}
     */
    public Local(int start, int length, int name, int desc, int index) {
        this.start = start;
        this.length = length;
        this.name = name;
        this.desc = desc;
        this.index = index;
    }

    @Override
    public int getLength() {
        // u2: start_pc
        // u2: length
        // u2: name_index
        // u2: descriptor_index
        // u2: index
        return 10;
    }
}
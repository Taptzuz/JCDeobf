package me.coley.bmf.attribute.method;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.util.Measurable;
import me.coley.bmf.util.MeasurableUtils;

/**
 * Local variable table attribute belonging to a {@link me.coley.bmf.MethodNode
 * method's} "{@link me.coley.bmf.attribute.method.AttributeCode Code}"
 * attribute.
 *
 * @author Matt
 */
public class LocalVariableTable implements Measurable {
    public List<Local> locals;

    public LocalVariableTable(int size) {
        locals = new ArrayList<Local>(size);
    }

    /**
     * Creates and adds a local variable to the table. See the documentation of
     * {@link me.coley.bmf.attribute.method.Local Local} for more information.
     * 
     * @param start
     *            Start index in the opcodes
     * @param len
     *            Length from start local lasts in the opcodes
     * @param name
     *            Constant pool pointers
     * @param desc
     *            Constant pool pointers
     * @param index
     *            Stack frame index.
     */
    public void add(int start, int len, int name, int desc, int index) {
        locals.add(new Local(start, len, name, desc, index));
    }

    @Override
    public int getLength() {
        // u2: num_locals
        // ??: locals
        return 2 + MeasurableUtils.getLength(locals);
    }
}

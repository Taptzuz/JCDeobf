package me.coley.bmf.attribute.clazz;

import me.coley.bmf.util.Measurable;

public class InnerClass implements Measurable {
    /**
     * Constant pool index of ConstClass of the inner class.
     */
    public int innerClassIndex;
    /**
     * Constant pool index of ConstClass of the outer class.
     */
    public int outerClassIndex;
    /**
     * Constant pool index of the UTF8 of the inner class's name.
     */
    public int innerName;
    /**
     * Constant pool index of the 
     */
    public int innerAccess;

    public InnerClass(int innerIndex, int outerIndex, int cInnerName, int innerAccess) {
        this.innerClassIndex = innerIndex;
        this.outerClassIndex = outerIndex;
        this.innerName = cInnerName;
        this.innerAccess = innerAccess;
    }

    @Override
    public int getLength() {
        // u2: inner_class_index
        // u2: outer_class_index
        // u2: inner_name
        // u2: inner_access
        return 8;
    }
}

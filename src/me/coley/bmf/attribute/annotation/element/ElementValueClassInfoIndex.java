package me.coley.bmf.attribute.annotation.element;

public class ElementValueClassInfoIndex extends ElementValue {
    /**
     * Points to a UTF constant pool entry indicating the return descriptor of
     * the class. <br>
     * Example:
     * <ul>
     * <li>v = Void.class
     * <li>Ljava/lang/Object; = Object.class
     * </ul>
     */
    public int classInfoIndex;

    public ElementValueClassInfoIndex(int classInfoIndex) {
        super(ElementValueType.CLASS_INFO_INDEX);
        this.classInfoIndex = classInfoIndex;
    }

    @Override
    public int getLength() {
        // u2: class_info_index
        return BASE_LEN + 2;
    }
}

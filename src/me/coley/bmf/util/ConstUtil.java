package me.coley.bmf.util;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.ClassNode;
import me.coley.bmf.consts.ConstClass;
import me.coley.bmf.consts.ConstUTF8;
import me.coley.bmf.consts.Constant;
import me.coley.bmf.consts.ConstantType;

public class ConstUtil {

    /**
     * Retrieves a string from the constant pool of a given ClassNode.
     * 
     * @param node
     * @param index
     * @return
     */
    public static String getUTF8(ClassNode node, int index) {
        return ((ConstUTF8) node.getConst(index)).getValue();
    }

    /**
     * Gets a name from a ConstClass at index i in a given node's constant pool.
     * 
     * @param node
     * @param classIndex
     * @return
     */
    public static String getClassName(ClassNode node, int classIndex) {
        return ((ConstUTF8) node.getConst(((ConstClass) node.getConst(classIndex)).getValue())).getValue();
    }

    /**
     * Returns a list of Constants from the given ClassNode of the given type.
     * 
     * @param cn
     * @param type
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Constant> List<T> getConstants(ClassNode cn, ConstantType type) {
        List<Constant> list = new ArrayList<Constant>();
        for (Constant c : cn.constants) {
            if (c == null)
                continue;
            if (c.type == type)
                list.add(c);
        }
        return (List<T>) list;
    }
}

package me.coley.bmf.signature;

import java.util.List;

import me.coley.bmf.mapping.InnerClassMapping;
import me.coley.bmf.util.Box;

public class SigArgClassWithInner extends SigArg {
    /**
     * The outer class's name
     */
    private final Box<String> outer;
    /**
     * The inner class's name
     */
    private final InnerClassMapping.InnerClassBox inner;
    /**
     * Generic args of classes with generic types.
     */
    private final List<SigArg> genericArgs;

    SigArgClassWithInner(Box<String> outer, InnerClassMapping.InnerClassBox inner, List<SigArg> genericArgs) {
        this.outer = outer;
        this.inner = inner;
        this.genericArgs = genericArgs;
    }

    @Override
    String toArg() {
        // Louter_name<args>.inner_name_section;
        StringBuilder builder = new StringBuilder();
        builder.append("L" + outer.getValue());
        if (genericArgs != null) {
            StringBuilder args = new StringBuilder();
            for (SigArg arg : genericArgs) {
                args.append(arg.toArg());
            }
            builder.append("<" + args.toString() + ">");
        }
        builder.append("." + inner.getInnerSection() + ";");
        return builder.toString();
    }
}

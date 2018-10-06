package me.coley.bmf.signature;

import java.util.List;

import me.coley.bmf.util.Box;

public class SigArgClass extends SigArg {
    /**
     * The class's name
     */
    private final Box<String> className;
    /**
     * Generic args of classes with generic types.
     */
    private final List<SigArg> genericArgs;

    SigArgClass(Box<String> className, List<SigArg> genericArgs) {
        this.className = className;
        this.genericArgs = genericArgs;
    }

    @Override
    String toArg() {
        if (genericArgs == null) {
            return className.getValue() + ";";
        } else {
            StringBuilder args = new StringBuilder();
            for (SigArg arg : genericArgs) {
                args.append(arg.toArg());
            }
            return className.getValue() + "<" + args.toString() + ">;";
        }
    }
}

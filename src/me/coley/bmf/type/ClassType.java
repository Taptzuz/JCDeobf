package me.coley.bmf.type;

import me.coley.bmf.util.Box;

public class ClassType extends Type {
    public final Box<String> className;

    public ClassType(Box<String> className) {
        super(Sort.CLASS);
        this.className = className;
    }

    @Override
    public String toDesc() {
        return "L" + className.getValue() + ";";
    }
}
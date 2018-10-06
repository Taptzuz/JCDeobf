package me.coley.bmf.attribute.clazz;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.util.Measurable;

public class BootstrapMethod implements Measurable {
    public int methodReference;
    public List<Integer> arguments;

    public BootstrapMethod(int methodReference, int argCount) {
        this.methodReference = methodReference;
        this.arguments = new ArrayList<Integer>(argCount);
    }

    public void addArgument(int arg) {
        arguments.add(arg);
    }

    @Override
    public int getLength() {
        // u2: method_ref
        // u2: num_args
        // u2[]: args
        return 4 + (2 * arguments.size());
    }

}

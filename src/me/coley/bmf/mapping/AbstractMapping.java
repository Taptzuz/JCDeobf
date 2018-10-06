package me.coley.bmf.mapping;

import me.coley.bmf.util.Box;

public class AbstractMapping {
    public final Box<String> name;

    public AbstractMapping(String name) {
        this(new Box<String>(name));
    }

    public AbstractMapping(Box<String> name) {
        this.name = name;
    }
}

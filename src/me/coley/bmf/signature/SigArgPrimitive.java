package me.coley.bmf.signature;

public class SigArgPrimitive extends SigArg {
    /**
     * Primitive desc (I, Z, F, etc.)
     */
    private final String desc;

    SigArgPrimitive(String desc) {
        this.desc = desc;
    }

    @Override
    String toArg() {
        return desc;
    }
}

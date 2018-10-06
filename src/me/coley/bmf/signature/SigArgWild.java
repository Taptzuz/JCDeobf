package me.coley.bmf.signature;

public class SigArgWild extends SigArg {
    private final SigArg inner;
    private final String wild;

    SigArgWild(String wild, SigArg inner) {
        this.wild = wild;
        this.inner = inner;
    }

    @Override
    String toArg() {
        String s = wild;
        if (inner != null) {
            s += inner.toArg();
        }
        return s;
    }
}

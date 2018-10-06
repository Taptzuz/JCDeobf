package me.coley.bmf.signature;

public class SigArgGeneric extends SigArg {
    /**
     * The name of the generic (T, K, V, etc.)
     */
    private final String varName;

    SigArgGeneric(String varName) {
        this.varName = varName;
    }

    @Override
    String toArg() {
        return "T" + varName + ";";
    }
}

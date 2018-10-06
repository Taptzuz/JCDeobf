package me.coley.bmf.signature;

abstract class SigArg {
    abstract String toArg();
    
    int length() {
        return toArg().length();
    }
}

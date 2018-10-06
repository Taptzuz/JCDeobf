package me.coley.bmf.consts.mapping;

import me.coley.bmf.consts.ConstUTF8;
import me.coley.bmf.signature.Signature;

public class ConstSignature extends ConstUTF8 {
    public Signature sig;

    public ConstSignature(Signature sig) {
        this.sig = sig;
    }

    @Override
    public String getValue() {
        return sig.toSignature();
    }

    @Override
    public void setValue(String value) {
        throw new RuntimeException("TODO: What should be done here?");
    }
}

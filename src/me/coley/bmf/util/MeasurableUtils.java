package me.coley.bmf.util;

import java.util.Collection;

public class MeasurableUtils {
    public static int getLength(Collection<? extends Measurable> items) {
        int i = 0;
        for (Measurable m : items) {
            i += m.getLength();
        }
        return i;
    }
}

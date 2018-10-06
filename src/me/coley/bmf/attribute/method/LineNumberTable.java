package me.coley.bmf.attribute.method;

import java.util.HashMap;
import java.util.Map;

import me.coley.bmf.util.Measurable;

public class LineNumberTable implements Measurable {
    public Map<Integer, Integer> indexToLine = new HashMap<>();

    public void add(int startPC, int lineNumber) {
        indexToLine.put(startPC, lineNumber);
    }

    @Override
    public int getLength() {
        // u2: num_lines
        // [u2: start_pc, u2: line_number]
        return 2 + (4 * indexToLine.size());
    }
}

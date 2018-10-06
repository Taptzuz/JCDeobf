package me.coley.bmf.attribute.annotation;

import java.util.List;

import me.coley.bmf.attribute.annotation.element.ElementValuePair;
import me.coley.bmf.util.Measurable;
import me.coley.bmf.util.MeasurableUtils;

public class Annotation implements Measurable {
    /**
     * Index in classpool that indicated the annotation's type.
     */
    public int type;

    /**
     * List of describing values.
     */
    public List<ElementValuePair> elementValuePairs;

    public Annotation(int type, List<ElementValuePair> elementValuePairs) {
        this.type = type;
        this.elementValuePairs = elementValuePairs;
    }

    @Override
    public int getLength() {
        // u2: type_index
        // u2: num_element_value_pairs
        // ??: element_value_pairs[num_element_value_pairs]
        return 4 + MeasurableUtils.getLength(elementValuePairs);
    }
}

package me.coley.bmf.attribute.annotation.element;

import java.util.List;

import me.coley.bmf.util.MeasurableUtils;

public class ElementValueArrayValue extends ElementValue {
    /**
     * Nested list of element values.
     */
    public List<ElementValue> values;

    public ElementValueArrayValue(List<ElementValue> values) {
        super(ElementValueType.ARRAY_VALUE);
        this.values = values;
    }

    @Override
    public int getLength() {
        // u2: num_values
        // ??: element_value values[num_values]
        return BASE_LEN + 2 + MeasurableUtils.getLength(values);
    }
}

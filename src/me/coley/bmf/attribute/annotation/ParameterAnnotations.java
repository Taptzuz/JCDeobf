package me.coley.bmf.attribute.annotation;

import java.util.List;

import me.coley.bmf.util.Measurable;
import me.coley.bmf.util.MeasurableUtils;

public class ParameterAnnotations implements Measurable {
    /**
     * List of annotations belonging to the parameter.
     */
    public List<Annotation> annotations;

    public ParameterAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public int getLength() {
        // u2: num_annotations
        // ??: annotations
        return 2 + MeasurableUtils.getLength(annotations);
    }
}

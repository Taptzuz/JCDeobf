package me.coley.bmf.attribute.annotation;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.util.MeasurableUtils;

public class AttributeParameterAnnotations extends Attribute {
    /**
     * Each entry represents the annotation on a parameter. The indices in the
     * list match up to indices of parameters in the method.
     */
    public List<ParameterAnnotations> parametersAnnotations;

    public AttributeParameterAnnotations(int name, boolean invisible, List<ParameterAnnotations> parameterAnnotations) {
        super(name, invisible ? AttributeType.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS
                : AttributeType.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS);
        this.parametersAnnotations = parameterAnnotations;
    }

    @Override
    public int getLength() {
        // u1: num_parameters
        // ??: parameter_annotations
        return 1 + MeasurableUtils.getLength(parametersAnnotations);
    }
}

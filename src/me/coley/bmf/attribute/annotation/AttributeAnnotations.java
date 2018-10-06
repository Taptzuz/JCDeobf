package me.coley.bmf.attribute.annotation;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.util.MeasurableUtils;

public class AttributeAnnotations extends Attribute {
    public List<Annotation> annotations;

    public AttributeAnnotations(int name, boolean invisible, List<Annotation> annotations) {
        super(name,
                invisible ? AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS : AttributeType.RUNTIME_VISIBLE_ANNOTATIONS);
        this.annotations = annotations;
    }

    @Override
    public int getLength() {
        // u2: num_annotations
        // ??: annotations
        return 2 + MeasurableUtils.getLength(annotations);
    }
}

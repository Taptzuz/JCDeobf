package me.coley.bmf;

import me.coley.bmf.attribute.*;
import me.coley.bmf.attribute.annotation.*;
import me.coley.bmf.util.ConstUtil;

/**
 * A member. Implementation is either a field or method.
 *
 * @author Matt
 */
public abstract class MemberNode implements AttributeOwner {
    /**
     * The class the member belongs to.
     */
    public final ClassNode owner;
    /**
     * Access. May be multiple combined.
     */
    public int access;
    /**
     * Index in the constant pool pointing to the member's name.
     */
    public int name;
    /**
     * Index in the constant pool pointing to the member's desc.
     */
    public int desc;
    /**
     * Attribute: If not-null, the member is deprecated.
     */
    public AttributeDeprecated deprecated;
    /**
     * Attribute: If not-null, the member is synthetic.
     */
    public AttributeSynthetic synthetic;
    /**
     * Attribute: Signature for generic information.
     */
    public AttributeSignature signature;
    /**
     * Attribute: Annotations
     */
    public AttributeAnnotations runtimeInvisibleAnnotations;
    /**
     * Attribute: Annotations
     */
    public AttributeAnnotations runtimeVisibleAnnotations;
    /**
     * Attribute: Annotations
     */
    public AttributeParameterAnnotations runtimeInvisibleParamAnnotations;
    /**
     * Attribute: Annotations
     */
    public AttributeParameterAnnotations runtimeVisibleParamAnnotations;

    /**
     * Creates the node and sets its owner.
     *
     * @param owner
     */
    public MemberNode(ClassNode owner) {
        this.owner = owner;
    }

    /**
     * Finds the member's name by accessing the owner-class's constant pool.
     * 
     * @return The member's name.
     */
    public String getName() {
        return ConstUtil.getUTF8(owner, name);
    }

    /**
     * Finds the member's descriptor by accessing the owner-class's constant
     * pool.
     * 
     * @return The member's descriptor.
     */
    public String getDesc() {
        return ConstUtil.getUTF8(owner, desc);
    }

    /**
     * Finds the member's signature by accessing the owner-class's constant
     * pool.
     * 
     * @return The member's signature. Null if none exists.
     */
    public String getSignature() {
        if (signature == null) {
            return null;
        }
        return ConstUtil.getUTF8(owner, signature.signature);
    }

    @Override
    public void addAttribute(Attribute attribute) {
        switch (attribute.type) {
        case SYNTHETIC:
            synthetic = (AttributeSynthetic) attribute;
            break;
        case SIGNATURE:
            signature = (AttributeSignature) attribute;
            break;
        case DEPRECATED:
            deprecated = (AttributeDeprecated) attribute;
            break;
        case RUNTIME_VISIBLE_ANNOTATIONS:
            runtimeVisibleAnnotations = (AttributeAnnotations) attribute;
            break;
        case RUNTIME_INVISIBLE_ANNOTATIONS:
            runtimeInvisibleAnnotations = (AttributeAnnotations) attribute;
            break;
        case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
            runtimeVisibleParamAnnotations = (AttributeParameterAnnotations) attribute;
            break;
        case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
            runtimeInvisibleParamAnnotations = (AttributeParameterAnnotations) attribute;
            break;
        default:
            break;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName().replace("Node", "") + ":" + access + ", "
                + owner.getConst(name).toString() + ", " + owner.getConst(desc);
    }
}

package me.coley.bmf;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.attribute.*;
import me.coley.bmf.attribute.annotation.AttributeAnnotations;
import me.coley.bmf.attribute.annotation.AttributeParameterAnnotations;
import me.coley.bmf.attribute.clazz.*;
import me.coley.bmf.consts.Constant;
import me.coley.bmf.util.ConstUtil;

/**
 * A class.
 *
 * @author Matt
 */
@SuppressWarnings("rawtypes")
public class ClassNode implements AttributeOwner {
    /**
     * Version info.
     */
    public int major = -1, minor = -1;
    /**
     * Access. May be multiple combined.
     */
    public int access;
    /**
     * Indices in the constant pool pointing to a class name.
     */
    public int classIndex, superIndex;
    /**
     * A list of indices in the constant pool pointing to classes that are
     * implemented.
     */
    public List<Integer> interfaceIndices = new ArrayList<>();
    ;
    /**
     * The constant pool.
     */
    public List<Constant> constants;
    /**
     * Fields.
     */
    public List<FieldNode> fields = new ArrayList<>();
    /**
     * Methods.
     */
    public List<MethodNode> methods = new ArrayList<>();
    ;
    /**
     * Attribute: If not-null, the class is deprecated.
     */
    public AttributeDeprecated deprecated;
    /**
     * Attribute: If not-null, the class is synthetic.
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
     * Attribute: Source file information
     */
    public AttributeSourceFile sourceFile;
    /**
     * Attribute: The method this <i>(anonymous)</i> class resides in.
     */
    public AttributeEnclosingMethod enclosingMethod;
    /**
     * Attribute: Inner classes
     */
    public AttributeInnerClasses innerClasses;
    /**
     * Attribute: InvokeDynamic bootstrap method information.
     */
    public AttributeBootstrapMethods bootstrapMethods;
    /**
     * Attribute: Debug information.
     */
    public AttributeSourceDebugExtension sourceDebug;

    /**
     * Finds the class's name in the constant pool. Returns as a string.
     *
     * @return Class's name.
     */
    public String getName() {
        return ConstUtil.getClassName(this, classIndex);
    }

    /**
     * Finds the class's super's name in the constant pool. Returns as a string.
     *
     * @return Class's super's name.
     */
    public String getSuperName() {
        return ConstUtil.getClassName(this, superIndex);
    }

    /**
     * Finds the class's signature in the constant pool. Returns as string.
     *
     * @return The class's signature. Null if none exists.
     */
    public String getSignature() {
        if (signature == null) {
            return null;
        }
        return ConstUtil.getUTF8(this, signature.signature);
    }

    public String getSourceFile() {
        if (sourceFile == null) {
            return null;
        }
        return ConstUtil.getUTF8(this, sourceFile.sourceFile);
    }

    /**
     * Checks if the class is an array type. Names of array classes will look
     * like field descriptors due to their usage of '[', 'L', and ';'. For
     * example an array of 'Apple' would be '[LApple;'.
     *
     * @return Class is array type.
     */
    public boolean isArray() {
        return getName().contains("[");
    }

    /**
     * Adds a constant to the constant pool.
     *
     * @param constant
     */
    public void addConst(Constant constant) {
        constants.add(constant);
    }

    /**
     * Retrieves a constant from the constant pool. Index starts at 1.
     *
     * @param index
     * @return
     */
    public Constant getConst(int index) {
        return constants.get(index - 1);
    }

    /**
     * Sets a constant at the given value to another constant.
     *
     * @param index
     * @param constant
     */
    public void setConst(int index, Constant constant) {
        constants.set(index - 1, constant);
    }

    /**
     * Sets and clears the constant pool to a given size.
     *
     * @param size
     */
    public void setPoolSize(int size) {
        constants = new ArrayList<Constant>(size);
    }

    /**
     * Adds an interface index to the class indices list.
     *
     * @param index
     */
    public void addInterfaceIndex(int index) {
        interfaceIndices.add(index);
    }

    /**
     * Adds a field to the class.
     *
     * @param field
     */
    public void addField(FieldNode field) {
        fields.add(field);
    }

    /**
     * Adds a method to the class.
     *
     * @param method
     */
    public void addMethod(MethodNode method) {
        methods.add(method);
    }

    @Override
    public void addAttribute(Attribute attribute) {
        switch (attribute.type) {
            case SOURCE_FILE:
                sourceFile = (AttributeSourceFile) attribute;
                break;
            case SOURCE_DEBUG_EXTENSION:
                sourceDebug = (AttributeSourceDebugExtension) attribute;
                break;
            case ENCLOSING_METHOD:
                enclosingMethod = (AttributeEnclosingMethod) attribute;
                break;
            case INNER_CLASSES:
                innerClasses = (AttributeInnerClasses) attribute;
                break;
            case BOOTSTRAP_METHODS:
                bootstrapMethods = (AttributeBootstrapMethods) attribute;
                break;
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
    public List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList<>();
        if (signature != null)
            attributes.add(signature);
        if (sourceFile != null)
            attributes.add(sourceFile);
        if (innerClasses != null)
            attributes.add(innerClasses);
        if (bootstrapMethods != null)
            attributes.add(bootstrapMethods);
        if (runtimeInvisibleAnnotations != null)
            attributes.add(runtimeInvisibleAnnotations);
        if (runtimeVisibleAnnotations != null)
            attributes.add(runtimeVisibleAnnotations);
        if (runtimeVisibleParamAnnotations != null)
            attributes.add(runtimeVisibleParamAnnotations);
        if (runtimeInvisibleParamAnnotations != null)
            attributes.add(runtimeInvisibleParamAnnotations);
        if (deprecated != null)
            attributes.add(deprecated);
        if (synthetic != null)
            attributes.add(synthetic);
        if (enclosingMethod != null)
            attributes.add(enclosingMethod);
        if (sourceDebug != null)
            attributes.add(sourceDebug);
        return attributes;
    }

    @Override
    public String toString() {
        String out = "  Version: " + major + "." + minor + "\n";
        out += "  Access: " + access + "\n";
        out += "  Class Index: " + getName() + "\n";
        out += "  Super Index: " + getSuperName() + "\n";
        out += "  Interfaces { ";
        for (int index : interfaceIndices) {
            out += getConst(index + 1).toString() + ", ";
        }
        int interfaceCut = interfaceIndices.size() == 0 ? 0 : 2;
        out = out.substring(0, out.length() - interfaceCut) + " }\n";
        out += "  Constant Pool {\n";
        for (Constant constant : constants) {
            out += "    " + constant.toString() + "\n";
        }
        out += "  }\n";
        out += "  Fields { \n";
        for (FieldNode field : fields) {
            out += "    " + field.toString() + "\n";
        }
        out += "  }\n";
        out += "  Methods { \n";
        for (MethodNode method : methods) {
            out += "    " + method.toString() + "\n";
        }
        out += "  }\n";
        return out;
    }

}
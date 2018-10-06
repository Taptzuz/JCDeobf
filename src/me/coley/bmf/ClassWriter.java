package me.coley.bmf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import me.coley.bmf.attribute.*;
import me.coley.bmf.attribute.annotation.*;
import me.coley.bmf.attribute.annotation.element.*;
import me.coley.bmf.attribute.clazz.*;
import me.coley.bmf.attribute.field.*;
import me.coley.bmf.attribute.method.*;
import me.coley.bmf.attribute.method.AttributeStackMapTable.*;
import me.coley.bmf.consts.*;

/**
 * @author Matt
 */
@SuppressWarnings("rawtypes")
public class ClassWriter {
    public static byte[] write(ClassNode node) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(baos);
        ds.writeInt(0xCAFEBABE);
        // Write version info
        ds.writeShort(node.minor);
        ds.writeShort(node.major);
        // Write the constant pool
        ds.writeShort(node.constants.size() + 1);
        for (Constant constant : node.constants) {
            if (constant == null) {
                // Doubles and longs take up two spaces, leaving one space null
                continue;
            }
            ds.writeByte(constant.type.getTag());
            writeConstant(ds, constant);
        }
        // Write access & index information
        ds.writeShort(node.access);
        ds.writeShort(node.classIndex);
        ds.writeShort(node.superIndex);
        // Write interfaces
        ds.writeShort(node.interfaceIndices.size());
        for (int interfaceIndex : node.interfaceIndices) {
            ds.writeShort(interfaceIndex);
        }
        // Write fields
        ds.writeShort(node.fields.size());
        for (FieldNode field : node.fields) {
            writeMember(ds, field);
        }
        // Write methods
        ds.writeShort(node.methods.size());
        for (MethodNode method : node.methods) {
            writeMember(ds, method);
        }
        // Write attributes
        List<Attribute> attribs = node.getAttributes();
        ds.writeShort(attribs.size());
        for (Attribute attrib : attribs) {
            writeAttribute(ds, attrib);
        }
        return baos.toByteArray();
    }

    private static void writeMember(DataOutputStream ds, MemberNode member) throws IOException {
        ds.writeShort(member.access);
        ds.writeShort(member.name);
        ds.writeShort(member.desc);
        List<Attribute> attribs = member.getAttributes();
        ds.writeShort(attribs.size());
        for (Attribute attrib : attribs) {
            writeAttribute(ds, attrib);
        }
    }

    private static void writeAttribute(DataOutputStream ds, Attribute attribute) throws IOException {
        ds.writeShort(attribute.name);
        ds.writeInt(attribute.getLength());
        switch (attribute.type) {
        case ANNOTATION_DEFAULT:
            AttributeAnnotationDefault annoDef = (AttributeAnnotationDefault) attribute;
            writeElementValue(annoDef.value, ds);
            break;
        case BOOTSTRAP_METHODS:
            AttributeBootstrapMethods bootstrap = (AttributeBootstrapMethods) attribute;
            ds.writeShort(bootstrap.methods.size());
            for (BootstrapMethod bsm : bootstrap.methods) {
                ds.writeShort(bsm.methodReference);
                ds.writeShort(bsm.arguments.size());
                for (int arg : bsm.arguments) {
                    ds.writeShort(arg);
                }
            }
            break;
        case CODE:
            AttributeCode code = (AttributeCode) attribute;
            ds.writeShort(code.maxStack);
            ds.writeShort(code.maxLocals);
            writeMethodCode(code.opcodes, ds);
            ds.writeShort(code.exceptions.size());
            for (MethodException me : code.exceptions) {
                ds.writeShort(me.start);
                ds.writeShort(me.end);
                ds.writeShort(me.handler);
                ds.writeShort(me.type);
            }
            int numCodeAttribs = 0;
            if (code.lines != null)
                numCodeAttribs++;
            if (code.stackMap != null)
                numCodeAttribs++;
            if (code.variables != null)
                numCodeAttribs++;
            if (code.variableTypes != null)
                numCodeAttribs++;
            ds.writeShort(numCodeAttribs);
            if (code.lines != null)
                writeAttribute(ds, code.lines);
            if (code.stackMap != null)
                writeAttribute(ds, code.stackMap);
            if (code.variables != null)
                writeAttribute(ds, code.variables);
            if (code.variableTypes != null)
                writeAttribute(ds, code.variableTypes);
            break;
        case CONSTANT_VALUE:
            AttributeConstantValue constVal = (AttributeConstantValue) attribute;
            ds.writeShort(constVal.constantIndex);
            break;
        case DEPRECATED:
            break;
        case ENCLOSING_METHOD:
            AttributeEnclosingMethod enclosing = (AttributeEnclosingMethod) attribute;
            ds.writeShort(enclosing.classIndex);
            ds.writeShort(enclosing.methodIndex);
            break;
        case EXCEPTIONS:
            AttributeExceptions exceptions = (AttributeExceptions) attribute;
            ds.writeShort(exceptions.exceptionIndicies.size());
            for (int exIndex : exceptions.exceptionIndicies) {
                ds.writeShort(exIndex);
            }
            break;
        case INNER_CLASSES:
            AttributeInnerClasses innerClass = (AttributeInnerClasses) attribute;
            ds.writeShort(innerClass.classes.size());
            for (InnerClass ic : innerClass.classes) {
                ds.writeShort(ic.innerClassIndex);
                ds.writeShort(ic.outerClassIndex);
                ds.writeShort(ic.innerName);
                ds.writeShort(ic.innerAccess);
            }
            break;
        case LINE_NUMBER_TABLE:
            AttributeLineNumberTable lines = (AttributeLineNumberTable) attribute;
            ds.writeShort(lines.lines.indexToLine.size());
            for (int startPC : lines.lines.indexToLine.keySet()) {
                ds.writeShort(startPC);
                ds.writeShort(lines.lines.indexToLine.get(startPC));
            }
            break;
        case LOCAL_VARIABLE_TABLE:
            AttributeLocalVariableTable locals = (AttributeLocalVariableTable) attribute;
            ds.writeShort(locals.variableTable.locals.size());
            for (Local loc : locals.variableTable.locals) {
                ds.writeShort(loc.start);
                ds.writeShort(loc.length);
                ds.writeShort(loc.name);
                ds.writeShort(loc.desc);
                ds.writeShort(loc.index);
            }
            break;
        case LOCAL_VARIABLE_TYPE_TABLE:
            AttributeLocalVariableTypeTable localTypes = (AttributeLocalVariableTypeTable) attribute;
            ds.writeShort(localTypes.localTypes.size());
            for (LocalVariableType loc : localTypes.localTypes) {
                ds.writeShort(loc.start);
                ds.writeShort(loc.length);
                ds.writeShort(loc.name);
                ds.writeShort(loc.signature);
                ds.writeShort(loc.index);
            }
            break;
        case RUNTIME_INVISIBLE_ANNOTATIONS:
        case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
        case RUNTIME_VISIBLE_ANNOTATIONS:
        case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
            writeAnnotations(attribute, ds);
            break;
        case SIGNATURE:
            AttributeSignature sig = (AttributeSignature) attribute;
            ds.writeShort(sig.signature);
            break;
        case SOURCE_DEBUG_EXTENSION:
            AttributeSourceDebugExtension debug = (AttributeSourceDebugExtension) attribute;
            for (int i : debug.data) {
                ds.writeByte(i);
            }
            break;
        case SOURCE_FILE:
            AttributeSourceFile source = (AttributeSourceFile) attribute;
            ds.writeShort(source.sourceFile);
            break;
        case STACK_MAP_TABLE:
            AttributeStackMapTable stack = (AttributeStackMapTable) attribute;
            ds.writeShort(stack.frames.size());
            for (Frame frame : stack.frames) {
                writeFrame(frame, ds);
            }
            break;
        case SYNTHETIC:
            break;
        default:
            throw new RuntimeException("Unhandled attribute! " + attribute.type);
        }

    }

    private static void writeFrame(Frame frame, DataOutputStream ds) throws IOException {
        int type = frame.getType();
        ds.write(type);
        if (type >= 0 && type <= 63) {
            // same_frame
        } else if (type >= 64 && type <= 127) {
            // same_locals_1_stack_item_frame
            writeVerificationType(((Frame.SameLocals1StackItem) frame).stack, ds);
        } else if (type == 247) {
            // same_locals_1_stack_item_frame_extended
            ds.writeShort(((Frame.SameLocals1StackItemExtended) frame).offsetDelta);
            writeVerificationType(((Frame.SameLocals1StackItemExtended) frame).stack, ds);
        } else if (type >= 248 && type <= 250) {
            // chop_frame
            ds.writeShort(((Frame.Chop) frame).offsetDelta);
        } else if (type == 251) {
            // same_frame_extended
            ds.writeShort(((Frame.SameExtended) frame).offsetDelta);
        } else if (type >= 252 && type <= 254) {
            // append_frame
            Frame.Append append = (Frame.Append) frame;
            ds.writeShort(append.offsetDelta);
            for (VerificationType v : append.locals) {
                writeVerificationType(v, ds);
            }
        } else if (type == 255) {
            // full_frame
            Frame.Full full = (Frame.Full) frame;
            ds.writeShort(full.offsetDelta);
            ds.writeShort(full.locals.size());
            for (VerificationType v : full.locals) {
                writeVerificationType(v, ds);
            }
            ds.writeShort(full.stack.size());
            for (VerificationType v : full.stack) {
                writeVerificationType(v, ds);
            }
        } else {
            throw new RuntimeException("Invalid frame type: " + type);
        }
    }

    private static void writeVerificationType(VerificationType v, DataOutputStream ds) throws IOException {
        int tag = v.tag;
        ds.write(tag);
        if (tag == 7) {
            // ObjectVariable
            ds.writeShort(((VerificationType.ObjectVariable) v).poolIndex);
        } else if (tag == 8) {
            // UninitializedVariable
            ds.writeShort(((VerificationType.UninitializedVariable) v).offset);
        }
    }

    private static void writeAnnotations(Attribute attribute, DataOutputStream ds) throws IOException {
        switch (attribute.type) {
        case RUNTIME_INVISIBLE_ANNOTATIONS:
        case RUNTIME_VISIBLE_ANNOTATIONS:
            AttributeAnnotations annos = (AttributeAnnotations) attribute;
            ds.writeShort(annos.annotations.size());
            for (Annotation anno : annos.annotations) {
                writeAnnotation(anno, ds);
            }
            break;
        case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
        case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
            AttributeParameterAnnotations pannos = (AttributeParameterAnnotations) attribute;
            ds.writeByte(pannos.parametersAnnotations.size());
            for (ParameterAnnotations anno : pannos.parametersAnnotations) {
                writeParameterAnnotations(anno, ds);
            }
            break;
        default:
            throw new RuntimeException("UNKNOWN ANNOTATION: " + attribute.type.name());
        }

    }

    private static void writeParameterAnnotations(ParameterAnnotations annos, DataOutputStream ds) throws IOException {
        ds.writeShort(annos.annotations.size());
        for (Annotation anno : annos.annotations) {
            writeAnnotation(anno, ds);
        }
    }

    private static void writeMethodCode(MethodCode opcodes, DataOutputStream ds) throws IOException {
        // TODO: Actually write opcodes (Pre-req: reading them correctly)
        ds.writeInt(opcodes.original.length);
        ds.write(opcodes.original);
    }

    private static void writeElementValue(ElementValue value, DataOutputStream ds) throws IOException {
        ds.writeByte(value.type.key);
        switch (value.type) {
        case CONST_VALUE_INDEX_BYTE:
        case CONST_VALUE_INDEX_C:
        case CONST_VALUE_INDEX_DOUBLE:
        case CONST_VALUE_INDEX_FLOAT:
        case CONST_VALUE_INDEX_INT:
        case CONST_VALUE_INDEX_LONG:
        case CONST_VALUE_INDEX_SHORT:
        case CONST_VALUE_INDEX_BOOLEAN:
        case CONST_VALUE_INDEX_s:
            ElementValueConstValueIndex constValInd = (ElementValueConstValueIndex) value;
            ds.writeShort(constValInd.constValueIndex);
            break;
        case ENUM_CONST_VALUE:
            // ElementValueConstValueIndex
            // cannot be cast to
            // ElementValueEnumConstValue
            ElementValueEnumConstValue constEnumVal = (ElementValueEnumConstValue) value;
            ds.writeShort(constEnumVal.type);
            ds.writeShort(constEnumVal.name);
            break;
        case CLASS_INFO_INDEX:
            ElementValueClassInfoIndex classInfo = (ElementValueClassInfoIndex) value;
            ds.writeShort(classInfo.classInfoIndex);
            break;
        case ANNOTATION_VALUE:
            ElementValueAnnotationValue annoVal = (ElementValueAnnotationValue) value;
            writeAnnotation(annoVal.annotation, ds);
            break;
        case ARRAY_VALUE:
            ElementValueArrayValue arrayVal = (ElementValueArrayValue) value;
            ds.writeShort(arrayVal.values.size());
            for (ElementValue ev : arrayVal.values) {
                writeElementValue(ev, ds);
            }
            break;
        }
    }

    private static void writeAnnotation(Annotation annotation, DataOutputStream ds) throws IOException {
        ds.writeShort(annotation.type);
        ds.writeShort(annotation.elementValuePairs.size());
        for (ElementValuePair evp : annotation.elementValuePairs) {
            ds.writeShort(evp.name);
            writeElementValue(evp.value, ds);
        }
    }
    

    private static void writeConstant(DataOutputStream ds, Constant constant) throws IOException {
        switch (constant.type) {
        case DOUBLE:
            ConstDouble constDouble = (ConstDouble) constant;
            ds.writeDouble(constDouble.getValue());
            break;
        case LONG:
            ConstLong constLong = (ConstLong) constant;
            ds.writeLong(constLong.getValue());
            break;
        case FLOAT:
            ConstFloat constFloat = (ConstFloat) constant;
            ds.writeFloat(constFloat.getValue());
            break;
        case INT:
            ConstInt constInt = (ConstInt) constant;
            ds.writeInt(constInt.getValue());
            break;
        case STRING:
            ConstString constString = (ConstString) constant;
            ds.writeShort(constString.getValue());
            break;
        case UTF8:
            ConstUTF8 constUTF = (ConstUTF8) constant;
            ds.writeUTF(constUTF.getValue());
            break;
        case CLASS:
            ConstClass constClass = (ConstClass) constant;
            ds.writeShort(constClass.getValue());
            break;
        case FIELD:
            ConstField constField = (ConstField) constant;
            ds.writeShort(constField.getClassIndex());
            ds.writeShort(constField.getNameTypeIndex());
            break;
        case METHOD:
            ConstMethod constMethod = (ConstMethod) constant;
            ds.writeShort(constMethod.getClassIndex());
            ds.writeShort(constMethod.getNameTypeIndex());
            break;
        case INTERFACE_METHOD:
            ConstInterfaceMethod constInterMethod = (ConstInterfaceMethod) constant;
            ds.writeShort(constInterMethod.getClassIndex());
            ds.writeShort(constInterMethod.getNameTypeIndex());
            break;
        case INVOKEDYNAMIC:
            ConstInvokeDynamic constInvokeDynamic = (ConstInvokeDynamic) constant;
            ds.writeShort(constInvokeDynamic.getBootstrapAttribute());
            ds.writeShort(constInvokeDynamic.getNameTypeIndex());
            break;
        case METHOD_HANDLE:
            ConstMethodHandle constMethodHandle = (ConstMethodHandle) constant;
            ds.writeByte(constMethodHandle.getKind());
            ds.writeShort(constMethodHandle.getIndex());
            break;
        case METHOD_TYPE:
            ConstMethodType constMethodType = (ConstMethodType) constant;
            ds.writeShort(constMethodType.getValue());
            break;
        case NAME_TYPE:
            ConstNameType constNameType = (ConstNameType) constant;
            ds.writeShort(constNameType.getNameIndex());
            ds.writeShort(constNameType.getDescIndex());
            break;
        default:
            break;

        }
    }
}

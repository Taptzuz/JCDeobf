package me.coley.bmf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.attribute.*;
import me.coley.bmf.attribute.annotation.*;
import me.coley.bmf.attribute.annotation.element.*;
import me.coley.bmf.attribute.clazz.*;
import me.coley.bmf.attribute.field.AttributeConstantValue;
import me.coley.bmf.attribute.method.*;
import me.coley.bmf.attribute.method.AttributeStackMapTable.*;
import me.coley.bmf.consts.*;
import me.coley.bmf.exception.InvalidClassException;
import me.coley.bmf.opcode.Opcode;
import me.coley.bmf.opcode.OpcodeInst;
import me.coley.bmf.opcode.impl.*;
import me.coley.bmf.util.IndexableDataStream;
import me.coley.bmf.util.StreamUtil;

/**
 * @author Matt
 */
@SuppressWarnings("rawtypes")
public class ClassReader {
    public static ClassNode getNode(byte[] data) throws InvalidClassException, IOException {
        IndexableDataStream is = StreamUtil.fromBytes(data);
        if (is.readInt() != 0xCAFEBABE) {
            throw new InvalidClassException("Does not start with 0xCAFEBABE");
        }
        // Create the node
        ClassNode node = new ClassNode();
        // Read version information
        node.minor = is.readUnsignedShort();
        node.major = is.readUnsignedShort();
        // Read the constant pool
        int poolIndex = 1;
        int poolLength = is.readUnsignedShort();
        node.setPoolSize(poolLength);
        while (poolIndex < poolLength) {
            ConstantType type = ConstantType.fromTag(is.readUnsignedByte());
            Constant constant = readConst(type, is);
            node.addConst(constant);
            if ((type == ConstantType.DOUBLE) || (type == ConstantType.LONG)) {
                // They take up 2 constant places in the pool.
                node.addConst(null);
                poolIndex++;
            }
            poolIndex++;
        }
        // Read access and index information
        node.access = is.readUnsignedShort();
        node.classIndex = is.readUnsignedShort();
        node.superIndex = is.readUnsignedShort();
        // Read interfaces
        int interfaceLength = is.readUnsignedShort();
        for (int i = 0; i < interfaceLength; i++) {
            node.addInterfaceIndex(is.readUnsignedShort());
        }
        // Read fields
        int fieldLength = is.readUnsignedShort();
        for (int i = 0; i < fieldLength; i++) {
            FieldNode field = readField(node, is);
            node.addField(field);
        }
        // Read methods
        int methodLength = is.readUnsignedShort();
        for (int i = 0; i < methodLength; i++) {
            MethodNode method = readMethod(node, is);
            node.addMethod(method);
        }
        // Read class attributes
        int attribsLength = is.readUnsignedShort();
        for (int i = 0; i < attribsLength; i++) {
            Attribute attribute = readAttribute(node, is);
            node.addAttribute(attribute);
        }
        return node;
    }

    private static FieldNode readField(ClassNode owner, IndexableDataStream is)
            throws IOException, InvalidClassException {
        FieldNode field = new FieldNode(owner);
        field.access = is.readUnsignedShort();
        field.name = is.readUnsignedShort();
        field.desc = is.readUnsignedShort();
        int attribsLength = is.readUnsignedShort();
        for (int i = 0; i < attribsLength; i++) {
            Attribute attribute = readAttribute(owner, is);
            field.addAttribute(attribute);
        }
        return field;
    }

    private static MethodNode readMethod(ClassNode owner, IndexableDataStream is)
            throws IOException, InvalidClassException {
        MethodNode method = new MethodNode(owner);
        method.access = is.readUnsignedShort();
        method.name = is.readUnsignedShort();
        method.desc = is.readUnsignedShort();
        int attribsLength = is.readUnsignedShort();
        for (int i = 0; i < attribsLength; i++) {
            Attribute attribute = readAttribute(owner, is);
            method.addAttribute(attribute);
        }
        return method;
    }

    private static Attribute readAttribute(ClassNode owner, IndexableDataStream is)
            throws IOException, InvalidClassException {
        int nameIndex = is.readUnsignedShort();
        int length = is.readInt();
        String name = owner.getConst(nameIndex).getValue().toString();
        AttributeType attributeType = AttributeType.fromName(name);
        if (attributeType == null) {
            throw new InvalidClassException("Unknown attribute: " + name);
        }
        switch (attributeType) {
        case ANNOTATION_DEFAULT: {
            return new AttributeAnnotationDefault(nameIndex, readElementValue(owner, is));
        }
        case BOOTSTRAP_METHODS: {
            int methods = is.readUnsignedShort();
            List<BootstrapMethod> bsMethods = new ArrayList<BootstrapMethod>(methods);
            for (int i = 0; i < methods; i++) {
                int methodRef = is.readUnsignedShort();
                int args = is.readUnsignedShort();
                BootstrapMethod bsm = new BootstrapMethod(methodRef, args);
                for (int a = 0; a < args; a++) {
                    bsm.addArgument(is.readUnsignedShort());
                }
                bsMethods.add(bsm);
            }
            return new AttributeBootstrapMethods(nameIndex, bsMethods);
        }
        case CODE: {
            int maxStack = is.readUnsignedShort();
            int maxLocals = is.readUnsignedShort();
            MethodCode codeData = readMethodCode(owner, is);
            List<MethodException> exceptions = new ArrayList<>();
            int exceptionLength = is.readUnsignedShort();
            for (int i = 0; i < exceptionLength; i++) {
                MethodException mexeption = new MethodException();
                int rangeStart = is.readUnsignedShort();
                int rangeEnd = is.readUnsignedShort();
                int rangeHandler = is.readUnsignedShort();
                int catchType = is.readUnsignedShort();
                mexeption.start = rangeStart;
                mexeption.end = rangeEnd;
                mexeption.handler = rangeHandler;
                mexeption.type = catchType;
                exceptions.add(mexeption);
            }
            List<Attribute> attributes = new ArrayList<>();
            int attributeLength = is.readUnsignedShort();
            for (int i = 0; i < attributeLength; i++) {
                Attribute attribute = readAttribute(owner, is);
                attributes.add(attribute);
            }
            return new AttributeCode(nameIndex, maxStack, maxLocals, exceptions, codeData, attributes);
        }
        case CONSTANT_VALUE: {
            int value = is.readUnsignedShort();
            return new AttributeConstantValue(nameIndex, value);
        }
        case DEPRECATED: {
            return new AttributeDeprecated(nameIndex);
        }
        case ENCLOSING_METHOD: {
            int classIndex = is.readUnsignedShort();
            int methodIndex = is.readUnsignedShort();
            return new AttributeEnclosingMethod(nameIndex, classIndex, methodIndex);
        }
        case EXCEPTIONS: {
            int exceptionCount = is.readUnsignedShort();
            List<Integer> exceptionIndices = new ArrayList<>();
            for (int i = 0; i < exceptionCount; i++) {
                exceptionIndices.add(is.readUnsignedShort());
            }
            return new AttributeExceptions(nameIndex, exceptionIndices);
        }
        case INNER_CLASSES: {
            int classCount = is.readUnsignedShort();
            List<InnerClass> classes = new ArrayList<>();
            for (int i = 0; i < classCount; i++) {
                int innerIndex = is.readUnsignedShort();
                int outerIndex = is.readUnsignedShort();
                int cInnerName = is.readUnsignedShort();
                int innerAccess = is.readUnsignedShort();
                classes.add(new InnerClass(innerIndex, outerIndex, cInnerName, innerAccess));
            }
            return new AttributeInnerClasses(nameIndex, classes);
        }
        case LINE_NUMBER_TABLE: {
            LineNumberTable table = new LineNumberTable();
            int tableLength = is.readUnsignedShort();
            for (int i = 0; i < tableLength; i++) {
                int startPC = is.readUnsignedShort();
                int lineNumber = is.readUnsignedShort();
                table.add(startPC, lineNumber);
            }
            return new AttributeLineNumberTable(nameIndex, table);
        }
        case LOCAL_VARIABLE_TABLE: {
            int tableLength = is.readUnsignedShort();
            LocalVariableTable locals = new LocalVariableTable(tableLength);
            for (int i = 0; i < tableLength; i++) {
                int lstartPC = is.readUnsignedShort();
                int llength = is.readUnsignedShort();
                int lname = is.readUnsignedShort();
                int ldesc = is.readUnsignedShort();
                int lindex = is.readUnsignedShort();
                locals.add(lstartPC, llength, lname, ldesc, lindex);
            }
            return new AttributeLocalVariableTable(nameIndex, locals);
        }
        case LOCAL_VARIABLE_TYPE_TABLE: {
            int tableLength = is.readUnsignedShort();
            List<LocalVariableType> localTypes = new ArrayList<>();
            for (int i = 0; i < tableLength; i++) {
                int lStart = is.readUnsignedShort();
                int lLen = is.readUnsignedShort();
                int lName = is.readUnsignedShort();
                int lSignature = is.readUnsignedShort();
                int lIndex = is.readUnsignedShort();
                localTypes.add(new LocalVariableType(lStart, lLen, lName, lSignature, lIndex));
            }
            return new AttributeLocalVariableTypeTable(nameIndex, localTypes);
        }

        case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
        case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
        case RUNTIME_VISIBLE_ANNOTATIONS:
        case RUNTIME_INVISIBLE_ANNOTATIONS: {
            return readAnnotations(owner, attributeType, is, nameIndex, length);
        }
        case SIGNATURE: {
            int sig = is.readUnsignedShort();
            return new AttributeSignature(nameIndex, sig);
        }
        case SOURCE_DEBUG_EXTENSION: {
            List<Integer> data = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                data.add(is.readUnsignedByte());
            }
            return new AttributeSourceDebugExtension(nameIndex, data);
        }
        case SOURCE_FILE: {
            int source = is.readUnsignedShort();
            return new AttributeSourceFile(nameIndex, source);
        }
        case STACK_MAP_TABLE: {
            return readStackMapTable(nameIndex, is);
        }
        case SYNTHETIC: {
            return new AttributeSynthetic(nameIndex);
        }
        default:
            break;
        }
        throw new RuntimeException("Unhandled attribute! " + attributeType);
    }

    private static MethodCode readMethodCode(ClassNode owner, IndexableDataStream is) throws IOException {
        int codeLength = is.readInt();
        // Store original opcode bytes
        byte[] origOpcodeBytes = new byte[codeLength];
        is.read(origOpcodeBytes);
        MethodCode codeData = new MethodCode(origOpcodeBytes);
        // TODO: Verify this on test cases
        boolean parseOpcodes = true;
        if (parseOpcodes) {
            // Create opcode data
            List<Opcode> opcodes = new ArrayList<>();
            IndexableDataStream opstr = StreamUtil.fromBytes(codeData.original);
            // Is there a way to find the # of opcodes? Can't seem to figure one
            // out aside from reading and knowing after the fact.
            while (opstr.available() > 0) {
                try {
                    Opcode op = readOpcode(opstr);
                    opcodes.add(op);
                } catch (Exception e) {
                    String className = owner.getName();
                    System.err.println("<" + className + "> Failed at (Skipped opcode parsing): " + opcodes.size());
                    e.printStackTrace();
                    return codeData;
                }
            }
            codeData.opcodes = opcodes;
            opstr.close();
        }
        return codeData;
    }

    private static Opcode readOpcode(IndexableDataStream is) throws IOException {
        int code = is.readUnsignedByte();
        switch (code) {
        case Opcode.NOP:
            return OpcodeInst.NOP;
        case Opcode.ACONST_NULL:
            return OpcodeInst.ACONST_NULL;
        case Opcode.ICONST_M1:
            return OpcodeInst.ICONST_M1;
        case Opcode.ICONST_0:
            return OpcodeInst.ICONST_0;
        case Opcode.ICONST_1:
            return OpcodeInst.ICONST_1;
        case Opcode.ICONST_2:
            return OpcodeInst.ICONST_2;
        case Opcode.ICONST_3:
            return OpcodeInst.ICONST_3;
        case Opcode.ICONST_4:
            return OpcodeInst.ICONST_4;
        case Opcode.ICONST_5:
            return OpcodeInst.ICONST_5;
        case Opcode.LCONST_0:
            return OpcodeInst.LCONST_0;
        case Opcode.LCONST_1:
            return OpcodeInst.LCONST_1;
        case Opcode.FCONST_0:
            return OpcodeInst.FCONST_0;
        case Opcode.FCONST_1:
            return OpcodeInst.FCONST_1;
        case Opcode.FCONST_2:
            return OpcodeInst.FCONST_2;
        case Opcode.DCONST_0:
            return OpcodeInst.DCONST_0;
        case Opcode.DCONST_1:
            return OpcodeInst.DCONST_1;
        case Opcode.BIPUSH:
            int byteVal = is.readUnsignedByte();
            return new BIPUSH(byteVal);
        case Opcode.SIPUSH:
            int shortVal = is.readUnsignedShort();
            return new SIPUSH(shortVal);
        case Opcode.LDC: {
            int index = is.readUnsignedByte();
            return new LDC(index);
        }
        case Opcode.LDC_W:
        case Opcode.LDC2_W: {
            int index = is.readUnsignedShort();
            return code == Opcode.LDC2_W ? new LDC2_W(index) : new LDC_W(index);
        }
        case Opcode.ILOAD: {
            int index = is.readUnsignedByte();
            return new ILOAD(index);
        }
        case Opcode.LLOAD: {
            int index = is.readUnsignedByte();
            return new LLOAD(index);
        }
        case Opcode.FLOAD: {
            int index = is.readUnsignedByte();
            return new FLOAD(index);
        }
        case Opcode.DLOAD: {
            int index = is.readUnsignedByte();
            return new DLOAD(index);
        }
        case Opcode.ALOAD: {
            int index = is.readUnsignedByte();
            return new ALOAD(index);
        }
        case Opcode.ILOAD_0:
            return OpcodeInst.ILOAD_0;
        case Opcode.ILOAD_1:
            return OpcodeInst.ILOAD_1;
        case Opcode.ILOAD_2:
            return OpcodeInst.ILOAD_2;
        case Opcode.ILOAD_3:
            return OpcodeInst.ILOAD_3;
        case Opcode.LLOAD_0:
            return OpcodeInst.LLOAD_0;
        case Opcode.LLOAD_1:
            return OpcodeInst.LLOAD_1;
        case Opcode.LLOAD_2:
            return OpcodeInst.LLOAD_2;
        case Opcode.LLOAD_3:
            return OpcodeInst.LLOAD_3;
        case Opcode.FLOAD_0:
            return OpcodeInst.FLOAD_0;
        case Opcode.FLOAD_1:
            return OpcodeInst.FLOAD_1;
        case Opcode.FLOAD_2:
            return OpcodeInst.FLOAD_2;
        case Opcode.FLOAD_3:
            return OpcodeInst.FLOAD_3;
        case Opcode.DLOAD_0:
            return OpcodeInst.DLOAD_0;
        case Opcode.DLOAD_1:
            return OpcodeInst.DLOAD_1;
        case Opcode.DLOAD_2:
            return OpcodeInst.DLOAD_2;
        case Opcode.DLOAD_3:
            return OpcodeInst.DLOAD_3;
        case Opcode.ALOAD_0:
            return OpcodeInst.ALOAD_0;
        case Opcode.ALOAD_1:
            return OpcodeInst.ALOAD_1;
        case Opcode.ALOAD_2:
            return OpcodeInst.ALOAD_2;
        case Opcode.ALOAD_3:
            return OpcodeInst.ALOAD_3;
        case Opcode.ISTORE: {
            int index = is.readUnsignedByte();
            return new ISTORE(index);
        }
        case Opcode.LSTORE: {
            int index = is.readUnsignedByte();
            return new LSTORE(index);
        }
        case Opcode.FSTORE: {
            int index = is.readUnsignedByte();
            return new FSTORE(index);
        }
        case Opcode.DSTORE: {
            int index = is.readUnsignedByte();
            return new DSTORE(index);
        }
        case Opcode.ASTORE: {
            int index = is.readUnsignedByte();
            return new ASTORE(index);
        }
        case Opcode.ISTORE_0:
            return OpcodeInst.ISTORE_0;
        case Opcode.ISTORE_1:
            return OpcodeInst.ISTORE_1;
        case Opcode.ISTORE_2:
            return OpcodeInst.ISTORE_2;
        case Opcode.ISTORE_3:
            return OpcodeInst.ISTORE_3;
        case Opcode.LSTORE_0:
            return OpcodeInst.LSTORE_0;
        case Opcode.LSTORE_1:
            return OpcodeInst.LSTORE_1;
        case Opcode.LSTORE_2:
            return OpcodeInst.LSTORE_2;
        case Opcode.LSTORE_3:
            return OpcodeInst.LSTORE_3;
        case Opcode.FSTORE_0:
            return OpcodeInst.FSTORE_0;
        case Opcode.FSTORE_1:
            return OpcodeInst.FSTORE_1;
        case Opcode.FSTORE_2:
            return OpcodeInst.FSTORE_2;
        case Opcode.FSTORE_3:
            return OpcodeInst.FSTORE_3;
        case Opcode.DSTORE_0:
            return OpcodeInst.DSTORE_0;
        case Opcode.DSTORE_1:
            return OpcodeInst.DSTORE_1;
        case Opcode.DSTORE_2:
            return OpcodeInst.DSTORE_2;
        case Opcode.DSTORE_3:
            return OpcodeInst.DSTORE_3;
        case Opcode.ASTORE_0:
            return OpcodeInst.ASTORE_0;
        case Opcode.ASTORE_1:
            return OpcodeInst.ASTORE_1;
        case Opcode.ASTORE_2:
            return OpcodeInst.ASTORE_2;
        case Opcode.ASTORE_3:
            return OpcodeInst.ASTORE_3;
        case Opcode.IALOAD:
            return OpcodeInst.IALOAD;
        case Opcode.LALOAD:
            return OpcodeInst.LALOAD;
        case Opcode.FALOAD:
            return OpcodeInst.FALOAD;
        case Opcode.DALOAD:
            return OpcodeInst.DALOAD;
        case Opcode.AALOAD:
            return OpcodeInst.AALOAD;
        case Opcode.BALOAD:
            return OpcodeInst.BALOAD;
        case Opcode.CALOAD:
            return OpcodeInst.CALOAD;
        case Opcode.SALOAD:
            return OpcodeInst.SALOAD;
        case Opcode.IASTORE:
            return OpcodeInst.IASTORE;
        case Opcode.LASTORE:
            return OpcodeInst.LASTORE;
        case Opcode.FASTORE:
            return OpcodeInst.FASTORE;
        case Opcode.DASTORE:
            return OpcodeInst.DASTORE;
        case Opcode.AASTORE:
            return OpcodeInst.AASTORE;
        case Opcode.BASTORE:
            return OpcodeInst.BASTORE;
        case Opcode.CASTORE:
            return OpcodeInst.CASTORE;
        case Opcode.SASTORE:
            return OpcodeInst.SASTORE;
        case Opcode.POP:
            return OpcodeInst.POP;
        case Opcode.POP2:
            return OpcodeInst.POP2;
        case Opcode.DUP:
            return OpcodeInst.DUP;
        case Opcode.DUP_X1:
            return OpcodeInst.DUP_X1;
        case Opcode.DUP_X2:
            return OpcodeInst.DUP_X2;
        case Opcode.DUP2:
            return OpcodeInst.DUP2;
        case Opcode.DUP2_X1:
            return OpcodeInst.DUP2_X1;
        case Opcode.DUP2_X2:
            return OpcodeInst.DUP2_X2;
        case Opcode.SWAP:
            return OpcodeInst.SWAP;
        case Opcode.IADD:
            return OpcodeInst.IADD;
        case Opcode.LADD:
            return OpcodeInst.LADD;
        case Opcode.FADD:
            return OpcodeInst.FADD;
        case Opcode.DADD:
            return OpcodeInst.DADD;
        case Opcode.ISUB:
            return OpcodeInst.ISUB;
        case Opcode.LSUB:
            return OpcodeInst.LSUB;
        case Opcode.FSUB:
            return OpcodeInst.FSUB;
        case Opcode.DSUB:
            return OpcodeInst.DSUB;
        case Opcode.IMUL:
            return OpcodeInst.ISUB;
        case Opcode.LMUL:
            return OpcodeInst.LSUB;
        case Opcode.FMUL:
            return OpcodeInst.FSUB;
        case Opcode.DMUL:
            return OpcodeInst.DSUB;
        case Opcode.IDIV:
            return OpcodeInst.ISUB;
        case Opcode.LDIV:
            return OpcodeInst.LSUB;
        case Opcode.FDIV:
            return OpcodeInst.FSUB;
        case Opcode.DDIV:
            return OpcodeInst.DSUB;
        case Opcode.IREM:
            return OpcodeInst.IREM;
        case Opcode.LREM:
            return OpcodeInst.LREM;
        case Opcode.FREM:
            return OpcodeInst.FREM;
        case Opcode.DREM:
            return OpcodeInst.DREM;
        case Opcode.INEG:
            return OpcodeInst.INEG;
        case Opcode.LNEG:
            return OpcodeInst.LNEG;
        case Opcode.FNEG:
            return OpcodeInst.FNEG;
        case Opcode.DNEG:
            return OpcodeInst.DNEG;
        case Opcode.ISHL:
            return OpcodeInst.ISHL;
        case Opcode.LSHL:
            return OpcodeInst.LSHL;
        case Opcode.ISHR:
            return OpcodeInst.ISHR;
        case Opcode.LSHR:
            return OpcodeInst.LSHR;
        case Opcode.IUSHR:
            return OpcodeInst.IUSHR;
        case Opcode.LUSHR:
            return OpcodeInst.LUSHR;
        case Opcode.IAND:
            return OpcodeInst.IAND;
        case Opcode.LAND:
            return OpcodeInst.LAND;
        case Opcode.IOR:
            return OpcodeInst.IOR;
        case Opcode.LOR:
            return OpcodeInst.LOR;
        case Opcode.IXOR:
            return OpcodeInst.IXOR;
        case Opcode.LXOR:
            return OpcodeInst.LXOR;
        case Opcode.IINC: {
            int index = is.readUnsignedByte();
            int increment = is.readUnsignedByte();
            return new IINC(index, increment);
        }
        case Opcode.I2L:
            return OpcodeInst.I2L;
        case Opcode.I2F:
            return OpcodeInst.I2F;
        case Opcode.I2D:
            return OpcodeInst.I2D;
        case Opcode.L2I:
            return OpcodeInst.L2I;
        case Opcode.L2F:
            return OpcodeInst.L2F;
        case Opcode.L2D:
            return OpcodeInst.L2D;
        case Opcode.F2I:
            return OpcodeInst.F2I;
        case Opcode.F2L:
            return OpcodeInst.F2L;
        case Opcode.F2D:
            return OpcodeInst.F2D;
        case Opcode.D2I:
            return OpcodeInst.D2I;
        case Opcode.D2L:
            return OpcodeInst.D2L;
        case Opcode.D2F:
            return OpcodeInst.D2F;
        case Opcode.I2B:
            return OpcodeInst.I2B;
        case Opcode.I2C:
            return OpcodeInst.I2C;
        case Opcode.I2S:
            return OpcodeInst.I2S;
        case Opcode.LCMP:
            return OpcodeInst.LCMP;
        case Opcode.FCMPL:
            return OpcodeInst.FCMPL;
        case Opcode.FCMPG:
            return OpcodeInst.FCMPG;
        case Opcode.DCMPL:
            return OpcodeInst.DCMPL;
        case Opcode.DCMPG:
            return OpcodeInst.DCMPG;
        case Opcode.IRETURN:
            return OpcodeInst.IRETURN;
        case Opcode.LRETURN:
            return OpcodeInst.LRETURN;
        case Opcode.FRETURN:
            return OpcodeInst.FRETURN;
        case Opcode.DRETURN:
            return OpcodeInst.DRETURN;
        case Opcode.ARETURN:
            return OpcodeInst.ARETURN;
        case Opcode.RETURN:
            return OpcodeInst.RETURN;
        case Opcode.IFEQ:
            return new IFEQ(is.readShort());
        case Opcode.IFNE:
            return new IFNE(is.readShort());
        case Opcode.IFLT:
            return new IFLT(is.readShort());
        case Opcode.IFGE:
            return new IFGE(is.readShort());
        case Opcode.IFGT:
            return new IFGT(is.readShort());
        case Opcode.IFLE:
            return new IFLE(is.readShort());
        case Opcode.IF_ICMPEQ:
            return new IF_ICMPEQ(is.readShort());
        case Opcode.IF_ICMPNE:
            return new IF_ICMPNE(is.readShort());
        case Opcode.IF_ICMPLT:
            return new IF_ICMPLT(is.readShort());
        case Opcode.IF_ICMPGE:
            return new IF_ICMPGE(is.readShort());
        case Opcode.IF_ICMPGT:
            return new IF_ICMPGT(is.readShort());
        case Opcode.IF_ICMPLE:
            return new IF_ICMPLE(is.readShort());
        case Opcode.IF_ACMPEQ:
            return new IF_ACMPEQ(is.readShort());
        case Opcode.IF_ACMPNE:
            return new IF_ACMPNE(is.readShort());
        case Opcode.IFNULL:
            return new IFNULL(is.readShort());
        case Opcode.IFNONNULL:
            return new IFNONNULL(is.readShort());
        case Opcode.GOTO:
        case Opcode.GOTO_W:
            return new GOTO(code == Opcode.GOTO_W ? is.readInt() : is.readShort());
        case Opcode.TABLESWITCH: {
            is.skip((4 - (is.getIndex() % 4)) % 4);
            int default_offset = is.readInt();
            int low = is.readInt();
            int high = is.readInt();
            int dif = (high - low + 1);
            List<Integer> offsets = new ArrayList<>();
            for (int i = 0; i < dif; i++) {
                int x = is.readInt();
                offsets.add(x);
            }
            return new TABLESWITCH(default_offset, low, high, offsets);
        }
        case Opcode.LOOKUPSWITCH: {
            is.skip((4 - (is.getIndex() % 4)) % 4);
            int default_offset = is.readInt();
            int n = is.readInt();
            List<LOOKUPSWITCH.OffsetPair> offsets = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int key = is.readInt();
                int offset = is.readInt();
                offsets.add(new LOOKUPSWITCH.OffsetPair(key, offset));
            }
            return new LOOKUPSWITCH(default_offset, offsets);
        }
        case Opcode.GETSTATIC:
            return new GETSTATIC(is.readUnsignedShort());
        case Opcode.PUTSTATIC:
            return new PUTSTATIC(is.readUnsignedShort());
        case Opcode.GETFIELD:
            return new GETFIELD(is.readUnsignedShort());
        case Opcode.PUTFIELD:
            return new PUTFIELD(is.readUnsignedShort());
        case Opcode.INVOKEVIRTUAL:
            return new INVOKEVIRTUAL(is.readUnsignedShort());
        case Opcode.INVOKESTATIC:
            return new INVOKESTATIC(is.readUnsignedShort());
        case Opcode.INVOKESPECIAL:
            return new INVOKESPECIAL(is.readUnsignedShort());
        case Opcode.INVOKEINTERFACE:
            return new INVOKEINTERFACE(is.readUnsignedShort(), is.readUnsignedByte(), is.readUnsignedByte());
        case Opcode.INVOKEDYNAMIC:
            INVOKEDYNAMIC indy = new INVOKEDYNAMIC(is.readUnsignedShort());
            // Discard two bytes that are always 0.
            is.readUnsignedByte();
            is.readUnsignedByte();
            return indy;
        case Opcode.NEW:
            return new NEW(is.readUnsignedShort());
        case Opcode.NEWARRAY:
            return new NEWARRAY(is.readUnsignedByte());
        case Opcode.ANEWARRAY:
            return new ANEWARRAY(is.readUnsignedShort());
        case Opcode.MULTIANEWARRAY:
            return new MULTIANEWARRAY(is.readUnsignedShort(), is.readUnsignedByte());
        case Opcode.ARRAYLENGTH:
            return OpcodeInst.ARRAYLENGTH;
        case Opcode.ATHROW:
            return OpcodeInst.ATHROW;
        case Opcode.CHECKCAST:
            return new CHECKCAST(is.readUnsignedShort());
        case Opcode.INSTANCEOF:
            return new INSTANCEOF(is.readUnsignedShort());
        case Opcode.MONITORENTER:
            return OpcodeInst.MONITORENTER;
        case Opcode.MONITOREXIT:
            return OpcodeInst.MONITOREXIT;
        case Opcode.BREAKPOINT:
            return OpcodeInst.BREAKPOINT;
        case Opcode.IMPDEP1:
            return OpcodeInst.IMPDEP1;
        case Opcode.IMPDEP2:
            return OpcodeInst.IMPDEP2;
        case Opcode.WIDE: {
            int opcode = is.readUnsignedByte();
            int varnum = is.readShort();
            // TODO: Verify this is actually checks the next opcode correctly.
            int next = is.readUnsignedByte();
            is.reset(1);
            if (next == Opcode.IINC) {
                int n = is.readShort();
                return new WIDE(opcode, varnum, n);
            } else {
                return new WIDE(opcode, varnum, null);
            }
        }
        case Opcode.RET:
        case Opcode.JSR:
        case Opcode.JSR_W:
            throw new RuntimeException("Unsupported: Outdated (Pre-Java 7) Opcode. Get with the times.");
        }
        // (TODO: Figure out why this happens and why it's super rare)
        System.out.println("UNKNOWN CODE: " + code + " ... Substituting with NOP");
        return OpcodeInst.NOP;
    }

    private static Attribute readAnnotations(ClassNode owner, AttributeType type, IndexableDataStream is, int nameIndex,
            int length) throws IOException {
        boolean param = (type == AttributeType.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS)
                || (type == AttributeType.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS);
        boolean invisible = (type == AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS)
                || (type == AttributeType.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS);
        int num = param ? is.readUnsignedByte() : is.readUnsignedShort();
        switch (type) {
        case RUNTIME_INVISIBLE_ANNOTATIONS:
        case RUNTIME_VISIBLE_ANNOTATIONS:
            List<Annotation> annotations = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                annotations.add(readAnnotation(owner, is));
            }
            return new AttributeAnnotations(nameIndex, invisible, annotations);
        case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
        case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
            List<ParameterAnnotations> paramAnnotations = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                paramAnnotations.add(readParameterAnnotations(owner, is));
            }
            return new AttributeParameterAnnotations(nameIndex, invisible, paramAnnotations);

        default:
            throw new RuntimeException("UNKNOWN ANNOTATION: " + type.name());
        }
    }

    private static Annotation readAnnotation(ClassNode owner, IndexableDataStream is) throws IOException {
        int typeIndex = is.readUnsignedShort();
        int num = is.readUnsignedShort();
        List<ElementValuePair> valuePairs = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int nameIndex = is.readUnsignedShort();
            ElementValue value = readElementValue(owner, is);
            valuePairs.add(new ElementValuePair(nameIndex, value));
        }
        return new Annotation(typeIndex, valuePairs);
    }

    private static ElementValue readElementValue(ClassNode owner, IndexableDataStream is) throws IOException {
        char tag = (char) is.readUnsignedByte();
        ElementValueType type = ElementValueType.fromType(tag);
        if (type == null) {
            throw new RuntimeException("UNKNOWN ANNOTATION ELEMENT TAG: " + tag);
        }
        switch (type) {
        case CONST_VALUE_INDEX_BYTE:
        case CONST_VALUE_INDEX_C:
        case CONST_VALUE_INDEX_DOUBLE:
        case CONST_VALUE_INDEX_FLOAT:
        case CONST_VALUE_INDEX_INT:
        case CONST_VALUE_INDEX_LONG:
        case CONST_VALUE_INDEX_SHORT:
        case CONST_VALUE_INDEX_BOOLEAN:
        case CONST_VALUE_INDEX_s:
            int constIndex = is.readUnsignedShort();
            return new ElementValueConstValueIndex(type, constIndex);
        case ENUM_CONST_VALUE:
            int typeIndex = is.readUnsignedShort();
            int constNameIndex = is.readUnsignedShort();
            return new ElementValueEnumConstValue(typeIndex, constNameIndex);
        case CLASS_INFO_INDEX:
            int infoIndex = is.readUnsignedShort();
            return new ElementValueClassInfoIndex(infoIndex);
        case ANNOTATION_VALUE:
            Annotation annotation = readAnnotation(owner, is);
            return new ElementValueAnnotationValue(annotation);
        case ARRAY_VALUE:
            int num = is.readUnsignedShort();
            List<ElementValue> values = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                values.add(readElementValue(owner, is));
            }
            return new ElementValueArrayValue(values);
        default:
            throw new RuntimeException("UNKNOWN ANNOTATION ELEMENT TYPE: " + type.name());

        }
    }

    private static ParameterAnnotations readParameterAnnotations(ClassNode owner, IndexableDataStream is)
            throws IOException {
        int num = is.readUnsignedShort();
        List<Annotation> annotations = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            annotations.add(readAnnotation(owner, is));
        }
        return new ParameterAnnotations(annotations);
    }

    private static Constant readConst(ConstantType constType, IndexableDataStream is) throws IOException {
        // Braces in switches are ugly, but having variable name pass-through
        // sucks.
        switch (constType) {
        case DOUBLE: {
            double value = is.readDouble();
            return new ConstDouble(value);
        }
        case LONG: {
            long value = is.readLong();
            return new ConstLong(value);
        }
        case FLOAT: {
            float value = is.readFloat();
            return new ConstFloat(value);
        }
        case INT: {
            int value = is.readInt();
            return new ConstInt(value);
        }
        case STRING: {
            int index = is.readUnsignedShort();
            return new ConstString(index);
        }
        case UTF8: {
            return new ConstUTF8(is.readUTF());
        }
        case CLASS: {
            int name = is.readUnsignedShort();
            return new ConstClass(name);
        }
        case FIELD: {
            int clazz = is.readUnsignedShort();
            int nameType = is.readUnsignedShort();
            return new ConstField(clazz, nameType);
        }
        case METHOD: {
            int clazz = is.readUnsignedShort();
            int nameType = is.readUnsignedShort();
            return new ConstMethod(clazz, nameType);
        }
        case INTERFACE_METHOD: {
            int clazz = is.readUnsignedShort();
            int nameType = is.readUnsignedShort();
            return new ConstInterfaceMethod(clazz, nameType);
        }
        case INVOKEDYNAMIC: {
            int attribute = is.readUnsignedShort();
            int nameType = is.readUnsignedShort();
            return new ConstInvokeDynamic(attribute, nameType);
        }
        case METHOD_HANDLE: {
            int kind = is.readUnsignedByte();
            int index = is.readUnsignedShort();
            return new ConstMethodHandle(kind, index);
        }
        case METHOD_TYPE: {
            int type = is.readUnsignedShort();
            return new ConstMethodType(type);
        }
        case NAME_TYPE: {
            int name = is.readUnsignedShort();
            int desc = is.readUnsignedShort();
            return new ConstNameType(name, desc);
        }
        default:
            break;
        }
        return null;
    }

    private static Attribute readStackMapTable(int nameIndex, IndexableDataStream is) throws IOException {
        int entries = is.readUnsignedShort();
        List<Frame> frames = new ArrayList<>();
        for (int i = 0; i < entries; i++) {
            int type = is.read();
            if (type >= 0 && type <= 63) {
                // same_frame
                frames.add(new Frame.Same(type));
            } else if (type >= 64 && type <= 127) {
                // same_locals_1_stack_item_frame
                frames.add(new Frame.SameLocals1StackItem(type, readVerificationType(is)));
            } else if (type == 247) {
                // same_locals_1_stack_item_frame_extended
                int offset = is.readUnsignedShort();
                frames.add(new Frame.SameLocals1StackItemExtended(offset, readVerificationType(is)));
            } else if (type >= 248 && type <= 250) {
                // chop_frame
                int offset = is.readUnsignedShort();
                frames.add(new Frame.Chop(type, offset));
            } else if (type == 251) {
                // same_frame_extended
                int offset = is.readUnsignedShort();
                frames.add(new Frame.SameExtended(offset));
            } else if (type >= 252 && type <= 254) {
                // append_frame
                int offset = is.readUnsignedShort();
                List<VerificationType> locals = new ArrayList<>();
                int size = type - 251;
                for (int j = 0; j < size; j++) {
                    locals.add(readVerificationType(is));
                }
                frames.add(new Frame.Append(type,offset, locals));
            } else if (type == 255) {
                // full_frame
                int offset = is.readUnsignedShort();
                int numLocals = is.readUnsignedShort();
                List<VerificationType> locals = new ArrayList<>();
                for (int j = 0; j < numLocals; j++) {
                    locals.add(readVerificationType(is));
                }
                int numStack = is.readUnsignedShort();
                List<VerificationType> stack = new ArrayList<>();
                for (int j = 0; j < numStack; j++) {
                    stack.add(readVerificationType(is));
                }
                frames.add(new Frame.Full(offset, locals, stack));
            } else {
                throw new RuntimeException("Invalid frame type: " + type);
            }
        }
        return new AttributeStackMapTable(nameIndex, frames);
    }

    private static VerificationType readVerificationType(IndexableDataStream is) throws IOException {
        int tag = is.read();
        switch (tag) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
            return new VerificationType(tag);
        case 7:
            return new VerificationType.ObjectVariable(is.readUnsignedShort());
        case 8:
            return new VerificationType.UninitializedVariable(is.readUnsignedShort());
        }
        return null;
    }
}
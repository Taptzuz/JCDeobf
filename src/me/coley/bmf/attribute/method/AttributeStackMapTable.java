package me.coley.bmf.attribute.method;

import java.util.List;

import me.coley.bmf.attribute.Attribute;
import me.coley.bmf.attribute.AttributeType;
import me.coley.bmf.util.Measurable;
import me.coley.bmf.util.MeasurableUtils;

public class AttributeStackMapTable extends Attribute {
    public List<Frame> frames;

    public AttributeStackMapTable(int name, List<Frame> frames) {
        super(name, AttributeType.STACK_MAP_TABLE);
        this.frames = frames;
    }

    @Override
    public int getLength() {
        // u2: number_of_entries
        return 2 + MeasurableUtils.getLength(frames);
    }

    public static class VerificationType implements Measurable {
        public int tag;

        public VerificationType(int tag) {
            this.tag = tag;
        }

        @Override
        public int getLength() {
            // u1: tag
            return 1;
        }

        public static class ObjectVariable extends VerificationType {
            public int poolIndex;

            public ObjectVariable(int poolIndex) {
                super(7);
                this.poolIndex = poolIndex;
            }

            @Override
            public int getLength() {
                // u1: tag
                // u2: cpool_index
                return 3;
            }
        }

        public static class UninitializedVariable extends VerificationType {
            public int offset;

            public UninitializedVariable(int offset) {
                super(8);
                this.offset = offset;
            }

            @Override
            public int getLength() {
                // u1: tag
                // u2: cpool_index
                return 3;
            }
        }

    }

    public abstract static class Frame implements Measurable {
        protected int type;

        public Frame(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static class Same extends Frame {
            public Same(int type) {
                super(type);
            }

            @Override
            public int getLength() {
                // u1: frame_type
                return 1;
            }
        }

        public static class SameExtended extends Same {
            public int offsetDelta;

            public SameExtended(int offsetDelta) {
                super(251);
                this.offsetDelta = offsetDelta;
            }

            @Override
            public int getLength() {
                // u1: frame_type
                // u2: offset_delta
                return 3;
            }
        }

        public static class SameLocals1StackItem extends Frame {
            public VerificationType stack;
            public int offsetDelta;

            public SameLocals1StackItem(int type, VerificationType stack) {
                super(type);
                offsetDelta = type - 64;
                this.stack = stack;
            }

            @Override
            public int getLength() {
                // u1: frame_type
                return 1 + stack.getLength();
            }
        }

        public static class SameLocals1StackItemExtended extends SameLocals1StackItem {
            public SameLocals1StackItemExtended(int offsetDelta, VerificationType stack) {
                super(247, stack);
                this.offsetDelta = offsetDelta;
            }
            
            @Override
            public int getLength() {
                // u1: frame_type
                // u2: offset_delta
                return 3 + stack.getLength();
            }
        }

        public static class Chop extends Frame {
            public int offsetDelta;

            public Chop(int type, int offsetDelta) {
                super(type);
                this.offsetDelta = offsetDelta;
                // offsetDelta = 251 - type;
            }

            @Override
            public int getLength() {
                // u1: frame_type
                // u2: offset_delta
                return 3;
            }
        }

        public static class Append extends Frame {
            public List<VerificationType> locals;
            public int offsetDelta;

            public Append(int type, int offsetDelta, List<VerificationType> locals) {
                super(type);
                this.offsetDelta = offsetDelta;
                this.locals = locals;
            }

            @Override
            public int getLength() {
                // u1: frame_type
                // u2: offset_delta
                return 3 + MeasurableUtils.getLength(locals);
            }
        }

        public static class Full extends Frame {
            public int offsetDelta;
            public List<VerificationType> locals, stack;

            public Full(int offsetDelta, List<VerificationType> locals, List<VerificationType> stack) {
                super(255);
                this.offsetDelta = offsetDelta;
                this.locals = locals;
                this.stack = stack;
            }

            @Override
            public int getLength() {
                // u1: frame_type
                // u2: offset_delta
                // u2: number_of_locals
                // u2: number_of_stack_items
                return 7 + MeasurableUtils.getLength(locals) + MeasurableUtils.getLength(stack);
            }
        }
    }
}

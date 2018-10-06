package me.coley.bmf.mapping;

import me.coley.bmf.util.Box;

public class InnerClassMapping extends ClassMapping {
    public InnerClassMapping(Box<String> outer, String inner) {
        super(createBox(outer, inner));
    }

    private static Box<String> createBox(Box<String> outer, String inner) {
        return new InnerClassBox(outer, inner);
    }

    public String getInnerSection() {
        return ((InnerClassBox) name).inner.getValue();
    }

    public static class InnerClassBox extends Box<String> {
        private final Box<String> outer;
        private Box<String> inner;

        public InnerClassBox(Box<String> outer, String inner) {
            super(inner);
            this.outer = outer;
            this.inner = new Box<String>(trim(inner));
        }
        
        public String getInnerSection() {
            return inner.getValue();
        }

        @Override
        public String getValue() {
            String nameOuter = outer.getValue();
            String nameInner = inner.getValue();
            // If the mapping for the inner contains the outer, it likely looks
            // is behaving like the following:
            // Outer: com.example.MyClass
            // Inner: com.example.MyClass$Inner
            // Make sure the output doesn't duplicate prefixes.
            if (nameInner.startsWith(nameOuter)) {
                int index = nameOuter.length();
                nameInner = nameInner.substring(index);
                if (nameInner.startsWith("$")) {
                    nameInner = nameInner.substring(1);
                }
            }
            return nameOuter + "$" + nameInner;
        }

        @Override
        public void setValue(String value) {
            this.inner.setValue(trim(value));
        }

        private static String trim(String value) {
            int index = value.lastIndexOf("$");
            if (index > 0) {
                value = value.substring(index + 1);
            }
            return value;
        }
    }
}

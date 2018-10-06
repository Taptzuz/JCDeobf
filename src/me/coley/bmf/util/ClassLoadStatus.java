package me.coley.bmf.util;

import java.io.InputStream;
import java.lang.reflect.Method;

public enum ClassLoadStatus {
    NOT_IN_JVM, LOAD_TRUE, LOAD_FALSE;

    /**
     * System ClassLoader
     */
    private static final ClassLoader SCL = ClassLoader.getSystemClassLoader();
    /**
     * System ClassLoader's findLoadedClass method
     */
    private static final Method SCL_FIND = getClassLoaderMethod("findLoadedClass", String.class);

    /**
     * Returns the load status of a class by its name.
     * 
     * @param className
     *            The internal class name.
     * @return ClassLoadStatus.
     */
    public final static ClassLoadStatus getLoadStatus(String className) {
        // Will throw an exception, instantly returning the NOT_IN_JVM constant.
        try (InputStream is = ClassLoader.getSystemResourceAsStream(className + ".class")) {
            // Cannot be read, not in JVM.
            if (is == null) {
                return ClassLoadStatus.NOT_IN_JVM;
            }
            // Invoke, check if exists
            Object find = SCL_FIND.invoke(SCL, className);
            if (find != null) {
                return ClassLoadStatus.LOAD_TRUE;
            }
        } catch (Exception e) {}
        // Exists, but not loaded.
        return ClassLoadStatus.LOAD_FALSE;
    }

    private final static Method getClassLoaderMethod(String name, Class<?>... args) {
        try {
            Method method = ClassLoader.class.getDeclaredMethod(name, args);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            return null;
        }
    }

}

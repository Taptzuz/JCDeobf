package me.coley.bmf.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import me.coley.bmf.ClassNode;
import me.coley.bmf.ClassWriter;
import me.coley.bmf.util.StreamUtil;

/**
 * A utility class for reading information from jar files.
 *
 * @author Matt
 */
public class JarUtil {
    /**
     * Manifest entry name.
     */
    private static final String MANIFEST_ENTRY_NAME = "META-INF/MANIFEST.MF";

    /**
     * Reads all classes in a jar file and stores them in a map of byte arrays.
     *
     * @param file
     * @return
     * @throws ZipException
     * @throws IOException
     */
    public static Map<String, byte[]> readJarClasses(File file) throws ZipException, IOException {
        return readJar(file, new NameFilter() {
            @Override
            public boolean matches(String name) {
                return name.endsWith(".class");
            }

            @Override
            public String filterName(String name) {
                return name.replace(".class", "");
            }
        });
    }

    /**
     * Reads all non-class entries in a jar file and stores them in a map of
     * byte arrays.
     *
     * @param file
     * @return
     * @throws ZipException
     * @throws IOException
     */
    public static Map<String, byte[]> readJarNonClasses(File file) throws ZipException, IOException {
        return readJar(file, new NameFilter() {
            @Override
            public boolean matches(String name) {
                return !name.endsWith(".class");
            }

            @Override
            public String filterName(String name) {
                return name;
            }
        });
    }

    /**
     * Returns a list of entry names based on the given File and NameFilter.
     *
     * @param file
     * @param filter
     * @return
     * @throws ZipException
     * @throws IOException
     */
    public static List<String> getEntryNames(File file, NameFilter filter) throws ZipException, IOException {
        List<String> names = new ArrayList<String>();
        ZipFile zip = new ZipFile(file);
        zip.stream().forEach(new Consumer<ZipEntry>() {
            @Override
            public void accept(ZipEntry entry) {
                String name = entry.getName();
                if (entry.isDirectory() || !filter.matches(name)) {
                    return;
                }
                names.add(filter.filterName(name));
            }
        });
        zip.close();
        return names;
    }

    /**
     * Reads all entries in a jar file and stores them in a map of byte arrays.
     * Certain entries may be excluded bases on the NameFilter parameter.
     *
     * @param file
     * @param filter
     * @return
     * @throws ZipException
     * @throws IOException
     */
    public static Map<String, byte[]> readJar(File file, NameFilter filter) throws ZipException, IOException {
        Map<String, byte[]> entries = new HashMap<>();
        ZipFile zip = new ZipFile(file);
        zip.stream().forEach(new Consumer<ZipEntry>() {
            @Override
            public void accept(ZipEntry entry) {
                String name = entry.getName();
                if (entry.isDirectory() || !filter.matches(name)) {
                    return;
                }
                try {
                    entries.put(filter.filterName(name), StreamUtil.fromStream(zip.getInputStream(entry)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        zip.close();
        return entries;
    }

    /**
     * Creates a new jar file given the original file <i>in</i>, the output file
     * <i>out</i>, and a map of ClassNodes.
     *
     * @param in
     * @param out
     * @param nodes
     */
    public static void writeJar(File in, File out, Map<String, ClassNode> nodes) {
        writeJar(in, out, nodes, null);
    }

    /**
     * Creates a new jar file given the original file <i>in</i>, the output file
     * <i>out</i>, a map of ClassNodes, and a map of non-class entries.
     *
     *
     * @param in
     * @param out
     * @param nodes
     * @param nonClasses
     */
    public static void writeJar(File in, File out, Map<String, ClassNode> nodes, Map<String, byte[]> nonClasses) {
        try {
            FileOutputStream fos = new FileOutputStream(out);
            Manifest manifest = getManifest(in);
            JarOutputStream jos = manifest != null ? new JarOutputStream(fos, manifest) : new JarOutputStream(fos);
            for (ClassNode node : nodes.values()) {
                JarEntry entry = new JarEntry(node.getName() + ".class");
                entry.setTime(System.currentTimeMillis());
                jos.putNextEntry(entry);
                jos.write(ClassWriter.write(node));
            }
            if (nonClasses != null) {
                if ((manifest != null) && nonClasses.containsKey(MANIFEST_ENTRY_NAME)) {
                    nonClasses.remove(MANIFEST_ENTRY_NAME);
                }
                for (String entryName : nonClasses.keySet()) {
                    JarEntry entry = new JarEntry(entryName);
                    entry.setTime(System.currentTimeMillis());
                    jos.putNextEntry(entry);
                    jos.write(nonClasses.get(entryName));
                }
            }
            jos.close();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Assumes the given file is a jar and retrieves the manifest.
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static Manifest getManifest(File in) throws IOException {
        JarFile jar = new JarFile(in);
        Manifest manifest = jar.getManifest();
        jar.close();
        return manifest;
    }
}
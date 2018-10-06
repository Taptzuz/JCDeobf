package me.finn.jcdeobf.deobfuscator;

import me.coley.bmf.ClassNode;
import me.coley.bmf.JarReader;
import me.finn.jcdeobf.gui.Gui;
import me.finn.jcdeobf.logger.Logger;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Finn on 05.10.2018
 */
public class Deobfuscator {

    public Deobfuscator(String input, String output) {
        HashMap<String, String> nameMap = new HashMap<>();
        long timer = System.currentTimeMillis();
        JarReader jarReader = new JarReader(new File(input), true, true);
        AtomicInteger removedCounter = new AtomicInteger(), renamedCounter = new AtomicInteger();

        int foundCounter = 0;

        for (ClassNode classNode : jarReader.getClassEntries().values()) {
            String sourceFileOnly = classNode.getSourceFile() != null ? "DEOBFUSCATED-NAMES/" + classNode.getSourceFile().substring(classNode.getSourceFile().lastIndexOf("/") + 1, classNode.getSourceFile().length() - 5) : classNode.getName();
            if (!classNode.getSourceFile().isEmpty()) {
                foundCounter++;
            }
            nameMap.put(classNode.getName(), sourceFileOnly);
        }

        if (foundCounter > 0) {
            Logger.logInfo("Found \"" + foundCounter + "\"" + " Source-File Attributes");

            Set<Object> duplicateMap = new HashSet<>();
            for (Iterator iterator = nameMap.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry entrySet = (Map.Entry) iterator.next();
                Object value = entrySet.getValue();
                if (!duplicateMap.add(value)) {
                    iterator.remove();
                    removedCounter.getAndIncrement();
                }
            }

            Logger.logInfo("Removed \"" + removedCounter + "\" duplicates");

            Logger.logInfo("Leftover Classes \"" + nameMap.size() + "\"");

            nameMap.forEach((key, value) -> {
                renamedCounter.getAndIncrement();
                jarReader.getMapping().getClassName(key).setValue(value);
            });

            Logger.logInfo("Renamed \"" + renamedCounter + "\" Classes to their Source-File Attributes!");
            jarReader.saveJarTo(new File(output));
            Logger.logInfo("Time Elapsed: " + (System.currentTimeMillis() - timer) + "ms.");
            Logger.logSuccess("File was saved to \"" + output + "\"!");
        } else {
            Gui.jTextArea.setText("");
            Logger.logError("No Source-File Attributes found!");
            nameMap.clear();
        }
    }
}

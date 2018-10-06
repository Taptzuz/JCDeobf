package me.finn.jcdeobf.logger;

import me.finn.jcdeobf.gui.Gui;

/**
 * Created by Finn on 05.10.2018
 */
public class Logger {

    public static void logInfo(String info) {
        Gui.jTextArea.append("[INFO] " + info + System.lineSeparator());
    }

    public static void logSuccess(String success) {
        Gui.jTextArea.append("[SUCCESS] " + success + System.lineSeparator());
    }

    public static void logError(String error) {
        Gui.jTextArea.append("[ERROR] " + error + System.lineSeparator());
    }
}

package me.finn.jcdeobf;

import me.finn.jcdeobf.gui.Gui;

import javax.swing.*;

/**
 * Created by Finn on 05.10.2018
 */
public class JCDeobf {

    //TODO: Progressbar, CLI support, improve code.

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Gui();
    }

    public static String getName() {
        return "JCDeobf";
    }

    public static float getVersion() {
        return 1.0F;
    }

    public static String getDeveloper() {
        return "Tapt/Finn";
    }

}

package me.finn.jcdeobf.utils;

import me.finn.jcdeobf.JCDeobf;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Finn on 06.10.2018
 */
public class InfoPanelUtil {

    public static void createInfoPanel(String fileName, String panelTitle, int width, int height) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(JCDeobf.class.getResourceAsStream(fileName)));
        JFrame frame = new JFrame(panelTitle);
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
        textArea.setEditable(false);
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            textArea.append(currentLine + "\n");
        }
        bufferedReader.close();
        frame.add(textArea);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

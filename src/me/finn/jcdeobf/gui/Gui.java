package me.finn.jcdeobf.gui;

import me.finn.jcdeobf.JCDeobf;
import me.finn.jcdeobf.deobfuscator.Deobfuscator;
import me.finn.jcdeobf.logger.Logger;
import me.finn.jcdeobf.utils.InfoPanelUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;

/**
 * Created by Finn on 05.10.2018
 */
public class Gui extends JFrame {

    public static JTextArea jTextArea = new JTextArea();

    public Gui() {
        setTitle(JCDeobf.getName() + " v." + JCDeobf.getVersion());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, 300, 335);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        setContentPane(jPanel);

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menuHelp = new JMenu("Help");
        JMenuItem itemAbout = new JMenuItem("About"), itemLicenses = new JMenuItem("Licenses");

        itemAbout.addActionListener(e -> {
            try {
                InfoPanelUtil.createInfoPanel("ABOUT", "About " + JCDeobf.getName() + " v." + JCDeobf.getVersion(), 285, 113);
            } catch (Exception e1) {
                Logger.logError(e1.getMessage());
            }
        });

        itemLicenses.addActionListener(e -> {
            try {
                InfoPanelUtil.createInfoPanel("LICENSE", "Licenses", 653, 300);
            } catch (Exception e1) {
                Logger.logError(e1.getMessage());
            }
        });

        jMenuBar.add(menuHelp);
        menuHelp.add(itemAbout);
        menuHelp.add(itemLicenses);
        setJMenuBar(jMenuBar);

        JLabel lblInput = new JLabel("Input:"), lblOutput = new JLabel("Output:"), lblWatermark = new JLabel("© " + JCDeobf.getDeveloper() + " 2018");
        JTextField inputField = new JTextField(), outputField = new JTextField();
        JButton btnInput = new JButton("..."), btnOutput = new JButton("..."), btnDeobfuscate = new JButton("Deobfuscate");

        lblInput.setBounds(10, 12, 40, 13);
        lblOutput.setBounds(10, 39, 43, 13);
        lblWatermark.setFont(new Font("Arial", Font.PLAIN, 12));
        lblWatermark.setBounds(192, 268, 270, 14);

        inputField.setBounds(58, 10, 170, 20);
        outputField.setBounds(58, 36, 170, 20);

        btnInput.setBounds(233, 11, 22, 18);
        btnOutput.setBounds(233, 36, 22, 18);
        btnDeobfuscate.setBounds(92, 60, 110, 22);

        jPanel.add(lblInput);
        jPanel.add(lblOutput);
        jPanel.add(lblWatermark);

        jPanel.add(inputField);
        jPanel.add(outputField);

        jPanel.add(btnInput);
        jPanel.add(btnOutput);
        jPanel.add(btnDeobfuscate);

        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setBounds(5, 87, 285, 180);
        jPanel.add(jScrollPane, BorderLayout.CENTER);

/*        JProgressBar jProgressBar = new JProgressBar();
        jProgressBar.setBounds(5, 270, 168, 17);
        jProgressBar.setStringPainted(true);
        jPanel.add(jProgressBar);*/

        btnInput.addActionListener(e ->

                openFileChooser("Select a Input-File!", true, inputField));
        btnOutput.addActionListener(e ->

                openFileChooser("Select a Output-File!", false, outputField));

        btnDeobfuscate.addActionListener(e ->

        {
            if (inputField.getText().isEmpty() || outputField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out all Fields!");
            } else {
                jTextArea.setText("");
                new Thread(() -> new Deobfuscator(inputField.getText(), outputField.getText())).start();
            }
        });

        setVisible(true);

    }

    private void openFileChooser(String title, boolean filter, JTextField jTextField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (filter) {
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".jar");
                }

                @Override
                public String getDescription() {
                    return "Only (.jar) Files!";
                }
            });
        }
        fileChooser.showOpenDialog(this);
        try {
            jTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        } catch (Exception e) {
            Logger.logInfo("No file selected!");
        }
    }
}

package com.github.appreciated.plandex_plugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class AddContextDialog {
    private final JPanel panel;
    private final JBTextField fileName;
    private final JBTextField directory;

    public AddContextDialog() {
        fileName = new JBTextField();
        directory = new JBTextField();

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Dateiname:"), fileName)
                .addLabeledComponent(new JBLabel("Verzeichnis:"), directory)
                .addComponent(new JButton("Hinzufügen"))
                .addComponent(new JButton("Abbrechen"))
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }
}

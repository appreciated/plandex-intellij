package com.github.appreciated.plandex_plugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class ReviewChangesDialog {
    private final JPanel panel;
    private final JList<String> changesList;

    public ReviewChangesDialog() {
        changesList = new JList<>(new DefaultListModel<>());

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Liste der Änderungen:"), new JScrollPane(changesList))
                .addComponent(new JButton("Anwenden"))
                .addComponent(new JButton("Ablehnen"))
                .addComponent(new JButton("Zurückspulen"))
                .addComponent(new JButton("Zurück"))
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }
}

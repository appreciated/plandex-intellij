package com.github.appreciated.plandex_plugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class ArchivePlanDialog {
    private final JPanel panel;

    public ArchivePlanDialog() {
        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Möchten Sie diesen Plan wirklich archivieren?"), new JPanel())
                .addComponent(new JButton("Archivieren"))
                .addComponent(new JButton("Abbrechen"))
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }
}

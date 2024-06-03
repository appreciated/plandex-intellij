package com.github.appreciated.plandex_plugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class PlanTaskDialog {
    private final JPanel panel;
    private final JComboBox<String> planSelection;
    private final JBTextField taskName;
    private final JBTextField description;
    private final JComboBox<String> priority;
    private final JBTextField tags;
    private final JList<String> contextFiles;

    public PlanTaskDialog() {
        planSelection = new JComboBox<>(new String[]{"Plan 1", "Plan 2", "New Plan"});
        taskName = new JBTextField();
        description = new JBTextField();
        priority = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        tags = new JBTextField();
        contextFiles = new JList<>(new DefaultListModel<>());

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Plan-Auswahl:"), planSelection)
                .addLabeledComponent(new JBLabel("Aufgabenname:"), taskName)
                .addLabeledComponent(new JBLabel("Beschreibung:"), description)
                .addLabeledComponent(new JBLabel("Priorität:"), priority)
                .addLabeledComponent(new JBLabel("Tags:"), tags)
                .addLabeledComponent(new JBLabel("Kontext-Dateien:"), new JScrollPane(contextFiles))
                .addComponent(new JButton("Erstellen"))
                .addComponent(new JButton("Abbrechen"))
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }
}

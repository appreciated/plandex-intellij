package com.github.appreciated.plandex_plugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class PlandexSettingsDialog implements Configurable {
    private JPanel panel;
    private JBTextField apiKeyField;
    private JBTextField defaultModelField;
    private JList<String> projectsList;
    private JList<String> modelsList;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Plandex Einstellungen";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        apiKeyField = new JBTextField();
        defaultModelField = new JBTextField();
        projectsList = new JList<>(new DefaultListModel<>());
        modelsList = new JList<>(new DefaultListModel<>());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Allgemein", createGeneralPanel());
        tabbedPane.addTab("Projekte", createProjectsPanel());
        tabbedPane.addTab("Modelle", createModelsPanel());

        panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGeneralPanel() {
        return FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("API-Schlüssel:"), apiKeyField)
                .addLabeledComponent(new JBLabel("Standardmodell:"), defaultModelField)
                .getPanel();
    }

    private JPanel createProjectsPanel() {
        return FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Verwaltete Projekte:"), new JScrollPane(projectsList))
                .addComponent(new JButton("Bearbeiten"))
                .getPanel();
    }

    private JPanel createModelsPanel() {
        return FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Modelle:"), new JScrollPane(modelsList))
                .addComponent(new JButton("Hinzufügen"))
                .addComponent(new JButton("Entfernen"))
                .getPanel();
    }

    @Override
    public boolean isModified() {
        // Implement logic to check if settings are modified
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        // Implement logic to apply settings
    }

    @Override
    public void reset() {
        // Implement logic to reset settings
    }

    @Override
    public void disposeUIResources() {
        panel = null;
    }
}

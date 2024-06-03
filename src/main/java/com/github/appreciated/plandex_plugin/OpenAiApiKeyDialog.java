package com.github.appreciated.plandex_plugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class OpenAiApiKeyDialog {
    private final JPanel panel;
    private final JBTextField apiKeyField;

    public OpenAiApiKeyDialog() {
        apiKeyField = new JBTextField();

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("OpenAI API-Schlüssel:"), apiKeyField)
                .addComponent(new JButton("Speichern"))
                .addComponent(new JButton("Abbrechen"))
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getApiKey() {
        return apiKeyField.getText();
    }
}

package com.github.appreciated.llm_project_quickstart_plugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

  private final JPanel myMainPanel;
  private final JBPasswordField openAiToken = new JBPasswordField();

  public AppSettingsComponent() {
    myMainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(new JBLabel("Open AI Token:"), openAiToken, 1, false)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
  }

  public JPanel getPanel() {
    return myMainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return openAiToken;
  }

  @NotNull
  public String getToken() {
    return openAiToken.getText();
  }

  public void setToken(@NotNull String token) {
    openAiToken.setText(token);
  }
}
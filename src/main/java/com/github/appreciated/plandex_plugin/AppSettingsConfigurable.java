package com.github.appreciated.plandex_plugin;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
final class AppSettingsConfigurable implements Configurable {

  private AppSettingsComponent settings;

  // A default constructor with no arguments is required because this implementation
  // is registered in an applicationConfigurable EP

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "OpenAI Api Key";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return settings.getPreferredFocusedComponent();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    settings = new AppSettingsComponent();
    return settings.getPanel();
  }

  @Override
  public boolean isModified() {
    AppSettingsState settings = AppSettingsState.getInstance();
      return !this.settings.getToken().equals(settings.openAiToken);
  }

  @Override
  public void apply() {
    AppSettingsState settings = AppSettingsState.getInstance();
    settings.openAiToken = this.settings.getToken();
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
    this.settings.setToken(settings.openAiToken);
  }

  @Override
  public void disposeUIResources() {
    settings = null;
  }

}
package com.github.appreciated.plandex_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import org.jetbrains.annotations.NotNull;

public class PlanTaskAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AppSettingsState instance = AppSettingsState.getInstance();
        if (instance.openAiToken == null || instance.openAiToken.isEmpty()) {
            Dialogs.showMissingOpenAiApiKey(e);
        } else {
            DialogBuilder dialogBuilder = new DialogBuilder(e.getProject());
            dialogBuilder.setTitle("Neue Aufgabe planen");
            // Add components to the dialog
            dialogBuilder.setCenterPanel(new PlanTaskDialog().getPanel());
            dialogBuilder.show();
        }
    }
}

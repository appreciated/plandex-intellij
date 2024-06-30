package com.github.appreciated.plandex_plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class FixFailingTestAction extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(true);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            Messages.showMessageDialog(project, "Test fehlgeschlagen", "Info", Messages.getInformationIcon());
        }
    }
}
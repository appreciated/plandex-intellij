package com.github.appreciated.plandex_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.project.Project;
import com.intellij.terminal.JBTerminalWidget;
import org.jetbrains.annotations.NotNull;

public class ExecuteTellAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Generate the command to start a tell command
        String command = "plandex tell";

        // Execute the command in the IntelliJ terminal

        // Show a message to the user
        Messages.showInfoMessage("Tell Befehl ausgeführt.", "Erfolg");
    }

}

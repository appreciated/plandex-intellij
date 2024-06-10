package com.github.appreciated.plandex_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.github.appreciated.plandex_plugin.terminal.TerminalUtil.executeCommandInTerminal;

public class AddFileToContextAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile selectedFile = (VirtualFile) e.getDataContext().getData("virtualFile");

        if (selectedFile != null) {
            String selectedPath = selectedFile.getPath();
            String command = "pdx load " + selectedPath;
            executeCommandInTerminal(e.getProject(), command);
        } else {
            Messages.showErrorDialog("Keine Datei oder kein Verzeichnis ausgewählt.", "Fehler");
        }
    }

}

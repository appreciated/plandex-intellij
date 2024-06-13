package com.github.appreciated.plandex_plugin.actions;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.github.appreciated.plandex_plugin.util.TerminalUtil.executeCommandInTerminal;

public class AddSelectionToPlandexContextAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile selectedFile = (VirtualFile) e.getDataContext().getData("virtualFile");

        if (selectedFile == null) {
            return; // oder eine geeignete Fehlerbehandlung
        }
        String modulePath = FileUtil.getCurrentModulePathFromProject(e.getProject(), selectedFile);
        String commandArgs = selectedFile.isDirectory() ? "--recursive" : "";
        executeCommandInTerminal(e.getProject(), selectedFile, "pdx load", commandArgs, modulePath);
    }
}
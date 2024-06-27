package com.github.appreciated.plandex_plugin.actions;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.appreciated.plandex_plugin.util.TerminalUtil.executeCommandForEachFileInTerminal;
import static com.github.appreciated.plandex_plugin.util.TerminalUtil.sendClear;

public class AddSelectionToContextAction extends AnAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectedFiles == null || selectedFiles.length == 0) {
            return; // or appropriate error handling
        }

        List<VirtualFile> fileList = Arrays.asList(selectedFiles);
        String modulePath = FileUtil.getCurrentModulePathFromProject(e.getProject(), selectedFiles[0]); // Assuming all files are in the same module
        String commandArgs = fileList.stream().allMatch(VirtualFile::isDirectory) ? "--recursive" : "";

        try {
            sendClear(e.getProject(), modulePath);
            executeCommandForEachFileInTerminal(e.getProject(), fileList, "pdx load", commandArgs, modulePath, true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

package com.github.appreciated.plandex_plugin.actions.editor;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;

import static com.github.appreciated.plandex_plugin.util.EditorSelectionUtil.isClassSelected;
import static com.github.appreciated.plandex_plugin.util.TerminalUtil.executeCommandForEachFileInTerminal;
import static com.github.appreciated.plandex_plugin.util.TerminalUtil.sendClear;

public class AddEditorSelectionToContextAction extends AnAction {

    public AddEditorSelectionToContextAction() {
        System.out.println();
    }

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isClassSelected(e));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (psiFile == null) {
            return; // or appropriate error handling
        }

        String modulePath = FileUtil.getCurrentModulePathFromProject(e.getProject(), psiFile.getVirtualFile()); // Assuming all files are in the same module

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                sendClear(e.getProject(), modulePath);
                executeCommandForEachFileInTerminal(e.getProject(), Collections.singletonList(psiFile.getVirtualFile()), "plandex load", "", modulePath, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}

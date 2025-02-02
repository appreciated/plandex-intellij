package com.github.appreciated.plandex_plugin.actions.editor;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static com.github.appreciated.plandex_plugin.util.EditorSelectionUtil.*;
import static com.github.appreciated.plandex_plugin.util.TerminalUtil.*;

public class AddTestForMethodAction extends AnAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isMethodSelected(e));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);

        PsiFile selectedFiles = e.getData(CommonDataKeys.PSI_FILE);
        VirtualFile virtualFile = selectedFiles.getVirtualFile();
        String modulePath = FileUtil.getCurrentModulePathFromProject(e.getProject(), virtualFile);
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                sendClear(e.getProject(), modulePath);
                executeCommandInTerminal(e.getProject(), "plandex new", modulePath, true);
                executeCommandForEachFileInTerminal(e.getProject(), List.of(virtualFile), "plandex l", "", modulePath, true);
                if (isPsiMethod(element)) {
                    String relativePath = VfsUtilCore.getRelativePath(virtualFile, ProjectUtil.guessProjectDir(e.getProject()));
                    executeCommandInTerminal(e.getProject(), "plandex tell \"Create a Test for the method %s in the class %s\"".formatted(getPsiMethodName(element), relativePath), modulePath, true);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}

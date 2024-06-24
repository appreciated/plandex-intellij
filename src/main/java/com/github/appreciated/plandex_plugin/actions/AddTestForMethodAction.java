package com.github.appreciated.plandex_plugin.actions;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.appreciated.plandex_plugin.util.TerminalUtil.executeCommandForEachFileInTerminal;
import static com.github.appreciated.plandex_plugin.util.TerminalUtil.executeCommandInTerminal;

public class AddTestForMethodAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);
        boolean isMethod = element != null && element.getClass().toString().contains("Method");
        e.getPresentation().setEnabledAndVisible(isMethod);
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
            executeCommandInTerminal(e.getProject(), "pdx new", modulePath, true);
            executeCommandForEachFileInTerminal(e.getProject(), List.of(virtualFile), "pdx l", "", modulePath, true);
            if (isPsiMethod(element)) {
                executeCommandInTerminal(e.getProject(), "pdx tell \"Create a Test for the method %s in the class %s\"".formatted(getPsiMethodName(element), virtualFile.getName()), modulePath, true);
            }
        });
    }

    private boolean isPsiMethod(PsiElement element) {
        if (element == null) {
            return false;
        }
        try {
            Method getNameMethod = element.getClass().getMethod("getName");
            return getNameMethod != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private String getPsiMethodName(PsiElement psiMethod) {
        try {
            Method getNameMethod = psiMethod.getClass().getMethod("getName");
            return (String) getNameMethod.invoke(psiMethod);
        } catch (Exception e) {
            e.printStackTrace();
            return "UnknownMethod";
        }
    }

}

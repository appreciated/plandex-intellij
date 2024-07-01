package com.github.appreciated.plandex_plugin.actions;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.execution.TestStateStorage;
import com.intellij.execution.codeInspection.TestFailedLineManagerImpl;
import com.intellij.execution.testframework.JavaTestLocator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.ClassUtil;
import com.intellij.testIntegration.TestFailedLineManager;

import java.io.IOException;
import java.util.List;

import static com.github.appreciated.plandex_plugin.util.EditorSelectionUtil.getPsiMethodName;
import static com.github.appreciated.plandex_plugin.util.TerminalUtil.*;

public class FixFailingTestAction extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(true);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
            PsiMethod element = (PsiMethod) e.getData(CommonDataKeys.PSI_ELEMENT);
            TestStateStorage instance = TestStateStorage.getInstance(project);
            PsiClass parent = (PsiClass) element.getParent();
            String url = JavaTestLocator.createLocationUrl(JavaTestLocator.TEST_PROTOCOL, parent.getQualifiedName(), element.getName());
            TestStateStorage.Record state = instance.getState(url);

            if (state != null) {
                VirtualFile virtualFile = file.getVirtualFile();
                if (virtualFile != null) {

                    String modulePath = FileUtil.getCurrentModulePathFromProject(e.getProject(), virtualFile);
                    try {
                        sendClear(e.getProject(), modulePath);
                        executeCommandForEachFileInTerminal(e.getProject(), List.of(file.getVirtualFile()), "plandex load", "", modulePath, true);
                        executeCommandInTerminal(
                                e.getProject(),
                                "plandex tell \"Make a change to the method %s in the class %s. <Your Prompty>\"".formatted(getPsiMethodName(element), modulePath),
                                modulePath,
                                false
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    Messages.showErrorDialog(project, "Failed to find the related file.", "Error");
                }
            } else {
                Messages.showMessageDialog(project, "No failing tests found.", "Info", Messages.getInformationIcon());
            }
        }
    }
}
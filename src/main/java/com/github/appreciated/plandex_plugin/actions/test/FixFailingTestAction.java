package com.github.appreciated.plandex_plugin.actions.test;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.execution.TestStateStorage;
import com.intellij.execution.testframework.JavaTestLocator;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerTestTreeView;
import com.intellij.execution.testframework.sm.runner.ui.SMTestRunnerResultsForm;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static com.github.appreciated.plandex_plugin.util.EditorSelectionUtil.getPsiMethodName;
import static com.github.appreciated.plandex_plugin.util.TerminalUtil.*;

public class FixFailingTestAction extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(true);
    }

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
            PsiMethod element = (PsiMethod) e.getData(CommonDataKeys.PSI_ELEMENT);
            if (file == null || element == null) {
                Messages.showErrorDialog(project, "No file or method selected.", "Error");
                return;
            }

            TestStateStorage instance = TestStateStorage.getInstance(project);
            PsiClass parent = (PsiClass) element.getParent();
            String url = JavaTestLocator.createLocationUrl(JavaTestLocator.TEST_PROTOCOL, parent.getQualifiedName(), element.getName());

            TestStateStorage.Record state = instance.getState(url);
            Content[] runnerContents = (Content[]) e.getDataContext().getData("runnerContents");
            SMTestRunnerResultsForm component = (SMTestRunnerResultsForm) runnerContents[0].getComponent();
            SMTRunnerTestTreeView treeView = (SMTRunnerTestTreeView) component.getTreeView();
            SMTestProxy selectedTest = treeView.getSelectedTest(treeView.getSelectionPath());
            String stackTrace = selectedTest.getStacktrace();

            if (state != null) {
                ApplicationManager.getApplication().executeOnPooledThread(() -> {
                    VirtualFile virtualFile = file.getVirtualFile();
                    if (virtualFile != null) {
                        String modulePath = FileUtil.getCurrentModulePathFromProject(e.getProject(), virtualFile);
                        try {
                            sendClear(e.getProject(), modulePath);
                            executeCommandForEachFileInTerminal(e.getProject(), List.of(file.getVirtualFile()), "plandex load", "", modulePath, true);
                            executeCommandInTerminal(
                                    e.getProject(),
                                    "plandex tell \"Fix the test method %s in the class %s. The stacktrace is as follows: %s\"".formatted(getPsiMethodName(element), modulePath, stackTrace),
                                    modulePath,
                                    false
                            );
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        Messages.showErrorDialog(project, "Failed to find the related file.", "Error");
                    }
                });
            } else {
                Messages.showMessageDialog(project, "No failing tests found.", "Info", Messages.getInformationIcon());
            }
        } else {
            Messages.showErrorDialog("Project is not available.", "Error");
        }
    }
}
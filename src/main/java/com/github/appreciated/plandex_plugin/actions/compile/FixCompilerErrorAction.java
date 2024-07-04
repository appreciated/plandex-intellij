package com.github.appreciated.plandex_plugin.actions.compile;

import com.github.appreciated.plandex_plugin.util.FileUtil;
import com.intellij.build.ExecutionNode;
import com.intellij.build.FileNavigatable;
import com.intellij.build.events.FileMessageEvent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.appreciated.plandex_plugin.util.TerminalUtil.*;

public class FixCompilerErrorAction extends AnAction {
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
            AtomicReference<VirtualFile> virtualFile = new AtomicReference<>();
            AtomicReference<String> errorMessage = new AtomicReference<>();
            Tree tree = (Tree) e.getDataContext().getData(PlatformCoreDataKeys.CONTEXT_COMPONENT);
            TreePath selectionPath = tree.getSelectionPath();
            Object selectedComponent = selectionPath.getLastPathComponent();
            if (selectedComponent instanceof DefaultMutableTreeNode) {
                ExecutionNode executionNode = (ExecutionNode) ((DefaultMutableTreeNode) selectedComponent).getUserObject();
                FileNavigatable navigable = (FileNavigatable) executionNode.getNavigatables().get(0);
                virtualFile.set(navigable.getFileDescriptor().getFile());
                errorMessage.set(String.valueOf(executionNode.getResult()));
                if (!executionNode.getChildList().isEmpty()) {
                    errorMessage.set(executionNode.getChildList().stream().map(executionNode1 -> {
                        FileMessageEvent ev = (FileMessageEvent) executionNode1.getResult();
                        return ev.getDescription();
                    }).reduce((s, s2) -> s + "\n" + s2).orElseThrow());
                } else {
                    FileMessageEvent ev = (FileMessageEvent) executionNode.getResult();
                    errorMessage.set(ev.getDescription());
                }
            }

            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                if (virtualFile.get() != null) {
                    String modulePath = FileUtil.getCurrentModulePathFromProject(e.getProject(), virtualFile.get());
                    try {
                        sendClear(e.getProject(), modulePath);
                        executeCommandForEachFileInTerminal(e.getProject(), List.of(virtualFile.get()), "plandex load", "", modulePath, true);
                        executeCommandInTerminal(
                                e.getProject(),
                                "plandex tell \"Fix the compiler error in the file %s with the message: %s\"".formatted(modulePath, errorMessage.get()),
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
            Messages.showErrorDialog("Project is not available.", "Error");
        }
    }
}

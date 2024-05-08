package com.github.appreciated.llm_project_quickstart_plugin;

import com.github.appreciated.interpreter.TaskInterpreter;
import com.github.appreciated.llm.ChatGptCommunicationService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class GenerateTopDownAction extends AnAction {

    private TaskInterpreter taskInterpreter;

    public GenerateTopDownAction() {
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        AppSettingsState instance = AppSettingsState.getInstance();
        if (instance.openAiToken == null || instance.openAiToken.isEmpty()) {
            DialogBuilder dialogBuilder = new DialogBuilder(e.getProject());
            dialogBuilder.setTitle("Missing Open AI API Token");
            JPanel dialogPanel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Please add a Open AI API Token under Settings/Tools/LLM Quick Start");
            label.setPreferredSize(new Dimension(130, 45));
            dialogPanel.add(label, BorderLayout.CENTER);
            dialogBuilder.setCenterPanel(dialogPanel);
            dialogBuilder.setOkActionEnabled(false);
            dialogBuilder.show();
        } else {
            taskInterpreter = new TaskInterpreter(new ChatGptCommunicationService(instance.openAiToken));
            try {
                try (InputStream resourceAsStream = getClass().getResourceAsStream("/Tasks.yaml")) {
                    assert resourceAsStream != null;
                    final VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
                    Path nioPath = virtualFile.toNioPath();
                    String yamlInput = new String(resourceAsStream.readAllBytes());
                    ApplicationManager.getApplication()
                            .executeOnPooledThread(() -> {
                                try {
                                    taskInterpreter.start(yamlInput, nioPath);
                                } catch (IOException | InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
package com.github.appreciated.llm_project_quickstart_plugin;


import com.github.appreciated.llm_task_definition_executor_core.interpreter.TaskInterpreter;
import com.github.appreciated.llm_task_definition_executor_core.llm.ChatGptCommunicationService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class GenerateTopDownAction extends AnAction {

    private Thread thread;
    private TaskInterpreter taskInterpreter;

    public GenerateTopDownAction() {
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        AppSettingsState instance = AppSettingsState.getInstance();
        if (instance.openAiToken == null || instance.openAiToken.isEmpty()) {
            showMissingOpenAiApiKey(e);
        } else {
            taskInterpreter = new TaskInterpreter(new ChatGptCommunicationService(instance.openAiToken));
            try {
                try (InputStream resourceAsStream = getClass().getResourceAsStream("/Tasks.yaml")) {
                    assert resourceAsStream != null;
                    final VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
                    Path nioPath = virtualFile.toNioPath();
                    String yamlInput = new String(resourceAsStream.readAllBytes());
                    startTasks(e, yamlInput, nioPath);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void showMissingOpenAiApiKey(AnActionEvent e) {
        DialogBuilder dialogBuilder = new DialogBuilder(e.getProject());
        dialogBuilder.setTitle("Missing Open AI API Token");
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Please add a Open AI API Token under Settings/Tools/LLM Quick Start");
        label.setPreferredSize(new Dimension(130, 45));
        dialogPanel.add(label, BorderLayout.CENTER);
        dialogBuilder.setCenterPanel(dialogPanel);
        dialogBuilder.setOkActionEnabled(false);
        dialogBuilder.show();
    }

    private void startTasks(AnActionEvent e, String yamlInput, Path nioPath) {
        ProgressManager.getInstance().run(new Task.Backgroundable(e.getProject(), "Generate Top Down", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(false);
                indicator.setFraction(0.1);
                thread = new Thread(() -> {
                    try {
                        taskInterpreter.start(yamlInput, nioPath, (s, s2, aFloat) -> {
                            indicator.setText(s);
                            indicator.setText2(s2);
                            indicator.setFraction(aFloat);
                        }, stringStringMap -> {
                            //TODO Add review dialog
                        });
                        indicator.setFraction(1.0);
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                thread.start();
                while (thread.isAlive()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    indicator.checkCanceled();
                }
            }

            @Override
            public void onCancel() {
                super.onCancel();
                if (thread != null) {
                    thread.interrupt();
                }
            }
        });
    }
}
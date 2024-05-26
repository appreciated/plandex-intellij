package com.github.appreciated.plandex_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static com.github.appreciated.plandex_plugin.Dialogs.showMissingOpenAiApiKey;

public class GenerateTopDownAction extends AnAction {

    private Thread thread;
    public GenerateTopDownAction() {
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        AppSettingsState instance = AppSettingsState.getInstance();
        if (instance.openAiToken == null || instance.openAiToken.isEmpty()) {
            showMissingOpenAiApiKey(e);
        } else {
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

    private void startTasks(AnActionEvent e, String yamlInput, Path nioPath) {
        ProgressManager.getInstance().run(new Task.Backgroundable(e.getProject(), "Generate ...", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(false);
                indicator.setFraction(0.1);
                thread = new Thread(() -> {
                    // execute Task ...
                    // Show Progress
                    indicator.setText("Title");
                    indicator.setText2("Subtitle");
                    indicator.setFraction(0.5); // 0-1.0
                    // when done
                    indicator.setFraction(1.0);
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
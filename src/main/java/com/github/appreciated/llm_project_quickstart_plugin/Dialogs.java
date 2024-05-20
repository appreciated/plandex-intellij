package com.github.appreciated.llm_project_quickstart_plugin;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Dialogs {
    public static void showMissingOpenAiApiKey(AnActionEvent e) {
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

    // Methode zur Anzeige des Dialogs
    public static void showFilesToBeReviewedDialog(Map<String, String> filesAndContentToBeReviewedMap) {
        new FileReviewDialog(filesAndContentToBeReviewedMap).showAndGet();
    }
}

package com.github.appreciated.llm_project_quickstart_plugin;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;

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

    public static void showFilesToBeReviewedDialog(Map<String, String> filesAndContentToBeReviewedMap) {
        DialogWrapper dialogWrapper = new DialogWrapper(true) {
            {
                setTitle("Files to be Reviewed");
                init();
            }

            @Override
            protected JComponent createCenterPanel() {
                JPanel panel = new JPanel(new BorderLayout());
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);

                StringBuilder content = new StringBuilder();
                filesAndContentToBeReviewedMap.forEach((fileName, fileContent) -> content.append("File: ").append(fileName).append("\n")
                        .append("Content: ").append(fileContent).append("\n\n"));

                textArea.setText(content.toString());
                panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
                return panel;
            }
        };

        dialogWrapper.showAndGet();
    }
}

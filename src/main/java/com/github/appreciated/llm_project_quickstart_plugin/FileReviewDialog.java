package com.github.appreciated.llm_project_quickstart_plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorFactoryImpl;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DetailsComponent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MasterDetailsComponent;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Map;

public class FileReviewDialog extends DialogWrapper {

    private final Map<String, String> filesAndContentToBeReviewedMap;


    public FileReviewDialog(Map<String, String> filesAndContentToBeReviewedMap) {
        super(true);
        this.filesAndContentToBeReviewedMap = filesAndContentToBeReviewedMap;
        setTitle("Files to be Reviewed");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        MyMasterDetailsComponent masterDetailsComponent = new MyMasterDetailsComponent(filesAndContentToBeReviewedMap);
        return masterDetailsComponent.createComponent();
    }

    private static class MyMasterDetailsComponent extends MasterDetailsComponent {
        private final Map<String, String> filesAndContentToBeReviewedMap;

        MyMasterDetailsComponent(Map<String, String> filesAndContentToBeReviewedMap) {
            this.filesAndContentToBeReviewedMap = filesAndContentToBeReviewedMap;
            initTree();

            getDetailsComponent().setDetailsModeEnabled(true);
        }

        @Override
        protected void initTree() {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("New Files");
            filesAndContentToBeReviewedMap.keySet().forEach(fileName -> {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileName);
                root.add(node);
            });
            myTree.setModel(new DefaultTreeModel(root));
            myTree.addTreeSelectionListener(e -> {
                TreePath path = e.getPath();
                DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object userObject = lastPathComponent.getUserObject();
                getDetails().setContent(getEditor(filesAndContentToBeReviewedMap.get(userObject)).getComponent());
            });
        }

        private static @NotNull Editor getEditor(String text) {
            EditorFactory editorFactory = EditorFactory.getInstance();
            Editor editor = editorFactory.createEditor(editorFactory.createDocument(text), null, FileTypes.PLAIN_TEXT, false);

            // Editor settings
            EditorSettings editorSettings = editor.getSettings();
            editorSettings.setLineNumbersShown(true);
            editorSettings.setFoldingOutlineShown(true);
            editorSettings.setRightMarginShown(false);
            return editor;
        }

        @Override
        protected String getEmptySelectionString() {
            return "Select a file to review its content";
        }

        @Override
        public @NlsContexts.ConfigurableName String getDisplayName() {
            return "";
        }
    }

    // Methode zur Anzeige des Dialogs
    public static void showFilesToBeReviewedDialog(Project project, Map<String, String> filesAndContentToBeReviewedMap) {
        new FileReviewDialog(filesAndContentToBeReviewedMap).showAndGet();
    }
}
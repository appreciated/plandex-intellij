package com.github.appreciated.plandex_plugin.project_pane;


import com.intellij.ide.SelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CustomProjectViewPane extends AbstractProjectViewPane implements DumbAware {

    public static final String CUSTOM_PROJECT_VIEW = "CUSTOM_PROJECT_VIEW";

    public CustomProjectViewPane(@NotNull Project project) {
        super(project);
    }

    @NotNull
    @Override
    public String getTitle() {
        return "Plandex Context";
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return new ImageIcon(); // Set your custom icon here
    }

    @NotNull
    @Override
    public String getId() {
        return CUSTOM_PROJECT_VIEW;
    }

    @Override
    public JComponent createComponent() {
        // Create and return your custom component here
        return new JLabel("This will show a list of the files in plandex context");
    }

    @Override
    public @NotNull ActionCallback updateFromRoot(boolean restoreExpandedPaths) {
        return ActionCallback.DONE;
    }

    @Override
    public void addToolbarActions(@NotNull DefaultActionGroup actionGroup) {
        // Add any toolbar actions here if necessary
    }

    @Override
    public void select(Object element, VirtualFile file, boolean requestFocus) {

    }

    @Override
    public @Nullable Object getData(@NotNull String dataId) {
        return null;
    }

    @Override
    public int getWeight() {
        return 10; // Set the weight to control the order of appearance
    }

    @Override
    public @NotNull SelectInTarget createSelectInTarget() {
        return new CustomSelectInTarget();
    }

    private static class CustomSelectInTarget implements SelectInTarget {

        @Override
        public boolean canSelect(@NotNull SelectInContext context) {
            return false;
        }

        @Override
        public void selectIn(@NotNull SelectInContext context, boolean requestFocus) {
            // Implement selection logic here
        }

        @Override
        public String getToolWindowId() {
            return CUSTOM_PROJECT_VIEW;
        }

        @Override
        public String getMinorViewId() {
            return CUSTOM_PROJECT_VIEW;
        }

        @Override
        public float getWeight() {
            return 10;
        }

        @Override
        public String toString() {
            return "Custom SelectInTarget";
        }
    }
}

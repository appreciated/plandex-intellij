package com.github.appreciated.plandex_plugin.util;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class FileUtil {
    public static @NotNull String getCurrentModulePathFromProject(Project project, VirtualFile selectedFile) {
        if (selectedFile != null && project != null) {
            Module module = ModuleUtilCore.findModuleForFile(selectedFile, project);
            if (module != null) {
                VirtualFile moduleDir = ProjectUtil.guessModuleDir(module);
                if (moduleDir != null) {
                    return moduleDir.getPath();
                } else {
                    Messages.showErrorDialog("Modulpfad konnte nicht gefunden werden.", "Fehler");
                }
            } else {
                Messages.showErrorDialog("Kein übergeordnetes Modul gefunden.", "Fehler");
            }
        } else {
            Messages.showErrorDialog("Kein Projekt gefunden.", "Fehler");
        }
        throw new IllegalStateException();
    }
}
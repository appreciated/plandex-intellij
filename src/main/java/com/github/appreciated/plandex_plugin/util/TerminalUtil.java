package com.github.appreciated.plandex_plugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.terminal.JBTerminalWidget;
import com.intellij.terminal.ui.TerminalWidget;
import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.model.TerminalTextBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;
import org.jetbrains.plugins.terminal.ShellStartupOptions;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalToolWindowManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class TerminalUtil {

    public static final String UBUNTU_22_04 = "Ubuntu-22.04";

    public static void executeCommandInTerminal(Project project, VirtualFile selectedFile, String command, String commandArgs, String workingDirectoryPath) {
        if (project != null) {
            TerminalToolWindowManager terminalToolWindowManager = TerminalToolWindowManager.getInstance(project);
            List<ShellTerminalWidget> relevantTerminalWidgets = getUbuntuTerminalWidgets(terminalToolWindowManager);
            if (!relevantTerminalWidgets.isEmpty()) {
                executeCommand(command, selectedFile, commandArgs, getUbuntuTerminalWidgets(terminalToolWindowManager).get(0));
            } else {
                createTerminalWithCommand(project, List.of("wsl.exe", "-d", UBUNTU_22_04), workingDirectoryPath, command);
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        executeCommand(command, selectedFile, commandArgs, getUbuntuTerminalWidgets(terminalToolWindowManager).get(0));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        }
    }

    private static @NotNull List<ShellTerminalWidget> getUbuntuTerminalWidgets(TerminalToolWindowManager terminalToolWindowManager) {
        Set<TerminalWidget> terminalWidgets = terminalToolWindowManager.getTerminalWidgets();
        return terminalWidgets.stream()
                .map(JBTerminalWidget::asJediTermWidget)
                .filter(widget -> widget instanceof ShellTerminalWidget)
                .map(widget -> (ShellTerminalWidget) widget)
                .filter(widget -> widget.getShellCommand() != null)
                .filter(widget -> widget.getShellCommand().stream().anyMatch(s -> s.contains("Ubuntu")))
                .toList();
    }

    private static void executeCommand(String command, VirtualFile selectedFile, String commandArgs, ShellTerminalWidget widget) {
        String actualCommand = "%s %s %s".formatted(command, calculateRelativePath(widget, selectedFile.getPath()), commandArgs);
        TtyConnector ttyConnector = widget.getTtyConnector();
        if (ttyConnector != null) {
            try {
                actualCommand +="\n";
                ttyConnector.write(actualCommand.getBytes());
            } catch (IOException ex) {
                Messages.showErrorDialog("Fehler beim Senden des Befehls: " + ex.getMessage(), "Fehler");
            }
        }
    }

    private static void createTerminalWithCommand(Project project, List<String> shellCommand, String workingDirectoryPath, String command) {
        TerminalToolWindowManager instance = TerminalToolWindowManager.getInstance(project);
        ToolWindow terminalToolWindow = instance.getToolWindow();
        if (terminalToolWindow != null) {
            LocalTerminalDirectRunner terminalRunner = new LocalTerminalDirectRunner(project);
            ShellStartupOptions startupOptions = new ShellStartupOptions.Builder()
                    .workingDirectory(workingDirectoryPath)
                    .shellCommand(shellCommand)
                    .build();
            TerminalWidget terminalWidget = terminalRunner.startShellTerminalWidget(Disposer.newDisposable(), startupOptions, true);
            instance.newTab(terminalToolWindow,terminalWidget);
            if (!terminalToolWindow.isVisible()) {
                terminalToolWindow.show(null);
            }
        }
    }

    protected static String calculateRelativePath(ShellTerminalWidget widget, String windowsFilePath) {
        TerminalTextBuffer typedShellCommand = widget.getTerminalTextBuffer();
        String line = typedShellCommand.getLine(0).getText();
        int lastPromptIndex = line.lastIndexOf('$');
        if (lastPromptIndex == -1) {
            return "Fehler: Kein gültiges Pfadende gefunden.";
        }
        String currentWslTerminalDir = line.substring(0, lastPromptIndex).trim();
        int lastColon = currentWslTerminalDir.lastIndexOf(':');
        if (lastColon != -1) {
            currentWslTerminalDir = currentWslTerminalDir.substring(lastColon + 1).trim();
        }

        // Entferne das Laufwerkspräfix, z.B. "/mnt/c" -> "/test2/"
        if (currentWslTerminalDir.startsWith("/mnt/")) {
            currentWslTerminalDir = currentWslTerminalDir.substring(6); // entfernt "/mnt/c"
            int firstSlash = currentWslTerminalDir.indexOf('/');
            if (firstSlash != -1) {
                currentWslTerminalDir = currentWslTerminalDir.substring(firstSlash+1);
            }
        }

        // Behandlung des Windows-Dateipfads: "C:\test\test2" -> "\test\test2"
        Path filePathPath;
        if (windowsFilePath.contains(":")) {
            filePathPath = Paths.get(windowsFilePath.substring(windowsFilePath.indexOf(":") + 2));
        } else {
            filePathPath = Paths.get(windowsFilePath);
        }

        Path basePath = Paths.get(currentWslTerminalDir);

        // Berechne den relativen Pfad
        String relativePath = basePath.relativize(filePathPath).toString();

        return convertPathToWSLStyle(relativePath);
    }

    /**
     * Konvertiert Windows-Pfadstile zu WSL-Pfadstilen (ersetzt Backslashes mit Slashes)
     * @param path Der Pfad, der konvertiert werden soll
     * @return Der konvertierte Pfad
     */
    private static String convertPathToWSLStyle(String path) {
        return path.replace('\\', '/');
    }
}
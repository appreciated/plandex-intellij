package com.github.appreciated.plandex_plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
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
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TerminalUtil {

    public static final String UBUNTU_22_04 = "Ubuntu-22.04";

    public static void executeCommandForEachFileInTerminal(Project project, List<VirtualFile> selectedFiles, String command, String commandArgs, String workingDirectoryPath, boolean addLineBreak) {
        if (project != null && !selectedFiles.isEmpty()) {
            TerminalToolWindowManager terminalToolWindowManager = TerminalToolWindowManager.getInstance(project);

            List<ShellTerminalWidget> ubuntuTerminalWidgets = getUbuntuTerminalWidgets(terminalToolWindowManager);
            if (!ubuntuTerminalWidgets.isEmpty()) {
                String finalCommand = selectedFiles.stream()
                        .map(file -> calculateRelativePath(ubuntuTerminalWidgets.get(0), file.getPath()))
                        .map(relativePath -> "%s %s %s".formatted(command, relativePath, commandArgs))
                        .collect(Collectors.joining("; "));

                executeCommandInTerminal(project, finalCommand, workingDirectoryPath, addLineBreak);
            } else {
                ApplicationManager.getApplication().invokeAndWait(() -> {
                    Messages.showErrorDialog("Kein Terminal vorhanden", "Fehler");
                    throw new RuntimeException("Kein Terminal vorhanden");
                });
            }
        }
    }

    public static void executeCommandInTerminal(Project project, String command, String workingDirectoryPath, boolean addLineBreak) {
        TerminalToolWindowManager terminalToolWindowManager = TerminalToolWindowManager.getInstance(project);
        List<ShellTerminalWidget> relevantTerminalWidgets = getUbuntuTerminalWidgets(terminalToolWindowManager);
        if (!relevantTerminalWidgets.isEmpty()) {
            executeCommand(command, relevantTerminalWidgets.get(0), addLineBreak);
        } else {
            createTerminalWithCommand(project, List.of("wsl.exe", "-d", UBUNTU_22_04), workingDirectoryPath, command);

            try {
                Thread.sleep(500);
                List<ShellTerminalWidget> ubuntuTerminalWidgets = getUbuntuTerminalWidgets(terminalToolWindowManager);
                if (!ubuntuTerminalWidgets.isEmpty()) {
                    executeCommand(command, ubuntuTerminalWidgets.get(0), addLineBreak);
                } else {
                    ApplicationManager.getApplication().invokeAndWait(() -> {
                        Messages.showErrorDialog("Terminal konnte nicht erstellt werden.", "Fehler");
                        throw new RuntimeException("Terminal konnte nicht erstellt werden.");
                    });
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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

    private static void executeCommand(String command, ShellTerminalWidget widget, boolean addLineBreak) {
        TtyConnector ttyConnector = widget.getTtyConnector();
        if (ttyConnector != null) {
            try {
                if (addLineBreak) {
                    command += "\n";
                }
                ttyConnector.write(command.getBytes());
                Thread.sleep(500);
                // Maximal 10 Sekunden warten
                for (int i = 0; i < 50; i++) {
                    if (checkIfCommandFinished(widget)) break;
                    Thread.sleep(200);
                }
            } catch (IOException | InterruptedException ex) {
                Messages.showErrorDialog("Error sending command: " + ex.getMessage(), "Error");
            }
        }
    }

    public static boolean checkIfCommandFinished(ShellTerminalWidget widget) {
        TerminalTextBuffer buffer = widget.getTerminalTextBuffer();
        String currentLine = buffer.getLine(widget.getTerminal().getCursorY() - 1).getText();
        return currentLine.matches(".*\\$.*");
    }

    private static void createTerminalWithCommand(Project project, List<String> shellCommand, String workingDirectoryPath, String command) {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            TerminalToolWindowManager instance = TerminalToolWindowManager.getInstance(project);
            ToolWindow terminalToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Terminal");
            if (terminalToolWindow != null) {
                LocalTerminalDirectRunner terminalRunner = new LocalTerminalDirectRunner(project);
                ShellStartupOptions startupOptions = new ShellStartupOptions.Builder()
                        .workingDirectory(workingDirectoryPath)
                        .shellCommand(shellCommand)
                        .build();

                TerminalWidget terminalWidget = terminalRunner.startShellTerminalWidget(Disposer.newDisposable(), startupOptions, true);
                instance.newTab(terminalToolWindow, terminalWidget);
                if (!terminalToolWindow.isVisible()) {
                    terminalToolWindow.show(null);
                }
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String calculateRelativePath(ShellTerminalWidget widget, String windowsFilePath) {
        TerminalTextBuffer buffer = widget.getTerminalTextBuffer();
        int index = widget.getTerminal().getCursorY() - 1;
        String currentLine = buffer.getLine(index).getText();
        int lastPromptIndex = currentLine.lastIndexOf('$');
        if (lastPromptIndex == -1) {
            return "Fehler: Kein gültiges Pfadende gefunden.";
        }
        String currentWslTerminalDir = currentLine.substring(0, lastPromptIndex).trim();
        int lastColon = currentWslTerminalDir.lastIndexOf(':');
        if (lastColon != -1) {
            currentWslTerminalDir = currentWslTerminalDir.substring(lastColon + 1).trim();
        }

        // Entferne das Laufwerkspräfix, z.B. "/mnt/c" -> "/test2/"
        if (currentWslTerminalDir.startsWith("/mnt/")) {
            currentWslTerminalDir = currentWslTerminalDir.substring(6); // entfernt "/mnt/c"
            int firstSlash = currentWslTerminalDir.indexOf('/');
            if (firstSlash != -1) {
                currentWslTerminalDir = currentWslTerminalDir.substring(firstSlash + 1);
            }
        }

        // Behandlung des Windows-Dateipfads: "C:\test\test2" -> "\test\test2"
        Path filePathPath;
        if (windowsFilePath.contains(":")) {
            filePathPath = Paths.get(windowsFilePath.substring(windowsFilePath.indexOf(":") + 2));
        } else {
            filePathPath = Paths.get(windowsFilePath);
        }

        try {
            Path basePath = Paths.get(currentWslTerminalDir);
            // Berechne den relativen Pfad
            String relativePath = basePath.relativize(filePathPath).toString();
            return convertPathToWSLStyle(relativePath);
        } catch (InvalidPathException e) {
            System.err.printf("The path '%s' caused an issue%n", currentLine);
            throw e;
        }
    }

    /**
     * Konvertiert Windows-Pfadstile zu WSL-Pfadstilen (ersetzt Backslashes mit Slashes)
     *
     * @param path Der Pfad, der konvertiert werden soll
     * @return Der konvertierte Pfad
     */
    private static String convertPathToWSLStyle(String path) {
        return path.replace('\\', '/');
    }
}
package com.github.appreciated.plandex_plugin.util;

import com.intellij.openapi.application.ApplicationManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TerminalUtil {

    public static void sendClear(Project project, String workingDirectoryPath) throws IOException {
        ShellTerminalWidget relevantTerminalWidget = getOrCreateUbuntuTerminalWidget(project, workingDirectoryPath);
        executeCommand(String.valueOf((char)3), relevantTerminalWidget, false);
    }

    public static void executeCommandForEachFileInTerminal(Project project, List<VirtualFile> selectedFiles, String command, String commandArgs, String workingDirectoryPath, boolean addLineBreak) throws IOException {
        if (project != null && !selectedFiles.isEmpty()) {
            ShellTerminalWidget relevantTerminalWidget = getOrCreateUbuntuTerminalWidget(project, workingDirectoryPath);
            String finalCommand = selectedFiles.stream()
                    .map(file -> calculateRelativePath(relevantTerminalWidget, file.getPath()))
                    .map(relativePath -> "%s %s %s".formatted(command, relativePath.contains(" ") ? "\"" + relativePath + "\"" : relativePath, commandArgs))
                    .collect(Collectors.joining("; "));

            executeCommandInTerminal(project, finalCommand, workingDirectoryPath, addLineBreak);
        }
    }

    public static void executeCommandInTerminal(Project project, String command, String workingDirectoryPath, boolean addLineBreak) throws IOException {
        ShellTerminalWidget relevantTerminalWidget = getOrCreateUbuntuTerminalWidget(project, workingDirectoryPath);
        executeCommand(command, relevantTerminalWidget, addLineBreak);
    }

    public static List<String> getShellCommand() throws IOException {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            Process process = new ProcessBuilder("wsl", "--list", "-q").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String ubuntuWsl = reader.lines().map(s -> s.replaceAll("\u0000", ""))
                    .filter(s -> s.toLowerCase().contains("ubuntu"))
                    .findFirst().orElseThrow();
            return List.of("wsl.exe", "-d", ubuntuWsl);
        } else {
            return List.of("bash");
        }
    }

    private static @NotNull ShellTerminalWidget getOrCreateUbuntuTerminalWidget(Project project, String workingDirectoryPath) throws IOException {
        TerminalToolWindowManager manager = TerminalToolWindowManager.getInstance(project);
        List<ShellTerminalWidget> ubuntuTerminalWidgets = getUbuntuTerminalWidgets(manager);
        if (ubuntuTerminalWidgets.isEmpty()) {
            createTerminalAtWorkingDir(project, getShellCommand(), workingDirectoryPath);
            ubuntuTerminalWidgets = getUbuntuTerminalWidgets(manager);
            if (ubuntuTerminalWidgets.isEmpty()) {
                throw new RuntimeException("Kein Terminal vorhanden");
            } else {
                return ubuntuTerminalWidgets.get(0);
            }
        } else {
            return ubuntuTerminalWidgets.get(0);
        }
    }

    private static @NotNull List<ShellTerminalWidget> getUbuntuTerminalWidgets(TerminalToolWindowManager manager) {
        Set<TerminalWidget> terminalWidgets = manager.getTerminalWidgets();
        return terminalWidgets.stream()
                .map(JBTerminalWidget::asJediTermWidget)
                .filter(widget -> widget instanceof ShellTerminalWidget)
                .map(widget -> (ShellTerminalWidget) widget)
                .filter(widget -> widget.getShellCommand() != null)
                .filter(widget -> widget.getShellCommand().stream().anyMatch(s -> s.toLowerCase().contains("Ubuntu".toLowerCase())))
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

    private static void createTerminalAtWorkingDir(Project project, List<String> shellCommand, String workingDirectoryPath) {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            try {
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
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
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
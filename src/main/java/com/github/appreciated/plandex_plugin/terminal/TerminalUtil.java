package com.github.appreciated.plandex_plugin.terminal;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.terminal.JBTerminalWidget;
import com.intellij.terminal.ui.TerminalWidget;
import com.jediterm.terminal.TtyConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;
import org.jetbrains.plugins.terminal.ShellStartupOptions;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalToolWindowManager;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class TerminalUtil {

    public static final String UBUNTU_22_04 = "Ubuntu-22.04";

    public static void executeCommandInTerminal(Project project, String command) {
        if (project != null) {
            TerminalToolWindowManager terminalToolWindowManager = TerminalToolWindowManager.getInstance(project);
            List<ShellTerminalWidget> relevantTerminalWidgets = getUbuntuTerminalWidgets(terminalToolWindowManager);
            if (!relevantTerminalWidgets.isEmpty()) {
                executeCommand(command, relevantTerminalWidgets.get(0));
            } else {
                createTerminalWithCommand(project, List.of("wsl.exe", "-d", UBUNTU_22_04), project.getBasePath(), command);
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        executeCommand(command, getUbuntuTerminalWidgets(terminalToolWindowManager).get(0));
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

    private static void executeCommand(String command, ShellTerminalWidget relevantTerminalWidget) {
        TtyConnector ttyConnector = relevantTerminalWidget.getTtyConnector();
        if (ttyConnector != null) {
            try {
                command +="\n";
                ttyConnector.write(command.getBytes());
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
}

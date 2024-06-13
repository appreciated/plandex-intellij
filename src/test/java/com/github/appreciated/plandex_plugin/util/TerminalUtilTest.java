package com.github.appreciated.plandex_plugin.util;

import com.jediterm.terminal.model.TerminalLine;
import com.jediterm.terminal.model.TerminalTextBuffer;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TerminalUtilTest {

    private ShellTerminalWidget mockWidget;
    private TerminalLine mockLine;

    @BeforeEach
    public void setUp() {
        mockWidget = mock(ShellTerminalWidget.class);
        TerminalTextBuffer mockTextBuffer = mock(TerminalTextBuffer.class);
        mockLine = mock(TerminalLine.class);

        when(mockWidget.getTerminalTextBuffer()).thenReturn(mockTextBuffer);
        when(mockTextBuffer.getLine(0)).thenReturn(mockLine);
    }

    @Test
    public void testCalculateRelativePath() {
        // Mock the terminal buffer
        when(mockLine.getText()).thenReturn("user@HOSTNAME:/mnt/c/Users/Example$");

        // Test case 1: Simple relative path
        String result = TerminalUtil.calculateRelativePath(mockWidget, "C:\\Users\\Example\\Documents\\file.txt");
        assertEquals("Documents/file.txt", result);

        // Test case 2: Different drive
        when(mockLine.getText()).thenReturn("user@HOSTNAME:/mnt/d/Projects$");
        result = TerminalUtil.calculateRelativePath(mockWidget, "D:\\Projects\\Code\\file.txt");
        assertEquals("Code/file.txt", result);

        // Test case 3: Root directory
        when(mockLine.getText()).thenReturn("user@HOSTNAME:/mnt/c$");
        result = TerminalUtil.calculateRelativePath(mockWidget, "C:\\file.txt");
        assertEquals("file.txt", result);
    }
}

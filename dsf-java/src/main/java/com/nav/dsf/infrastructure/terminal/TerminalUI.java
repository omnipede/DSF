package com.nav.dsf.infrastructure.terminal;

import com.nav.dsf.common.util.DateUtil;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * Terminal UI manager for DSF application.
 * Provides console input/output capabilities similar to CICS terminal handling.
 * 
 * This replaces the PL/I EXEC CICS SEND/RECEIVE MAP operations.
 */
public class TerminalUI implements AutoCloseable {
    
    private static final String BORDER_HORIZONTAL = "─";
    private static final String BORDER_VERTICAL = "│";
    private static final String BORDER_CORNER_TL = "┌";
    private static final String BORDER_CORNER_TR = "┐";
    private static final String BORDER_CORNER_BL = "└";
    private static final String BORDER_CORNER_BR = "┘";
    private static final String BORDER_T_JUNCTION = "├";
    private static final String BORDER_L_JUNCTION = "┤";
    
    private final Terminal terminal;
    private final LineReader reader;
    private final int width;
    private final int height;

    public TerminalUI() throws IOException {
        this.terminal = TerminalBuilder.builder()
            .system(true)
            .build();
        
        this.reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .build();
        
        this.width = terminal.getWidth();
        this.height = terminal.getHeight();
    }

    /**
     * Clears the terminal screen.
     */
    public void clearScreen() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    /**
     * Prints a line of text to the terminal.
     */
    public void print(String text) {
        terminal.writer().print(text);
        terminal.flush();
    }

    /**
     * Prints a line of text with a newline.
     */
    public void println() {
        terminal.writer().println();
        terminal.flush();
    }

    /**
     * Prints a line of text with a newline.
     */
    public void println(String text) {
        terminal.writer().println(text);
        terminal.flush();
    }

    /**
     * Reads a line of input from the user.
     */
    public String readLine() {
        try {
            return reader.readLine("");
        } catch (UserInterruptException e) {
            return null;
        }
    }

    /**
     * Reads a line of input with a prompt.
     */
    public String readLine(String prompt) {
        try {
            return reader.readLine(prompt + " ");
        } catch (UserInterruptException e) {
            return null;
        }
    }

    /**
     * Reads a single character from the user.
     */
    public char readChar() {
        try {
            int c = terminal.reader().read();
            return (char) c;
        } catch (IOException e) {
            return '\0';
        }
    }

    /**
     * Waits for the user to press Enter.
     */
    public void pressEnterToContinue() {
        print("Press Enter to continue...");
        readLine();
    }

    /**
     * Draws a horizontal line.
     */
    public void drawHorizontalLine(int width) {
        println(BORDER_HORIZONTAL.repeat(width));
    }

    /**
     * Draws a box with a title.
     */
    public void drawBox(String[] content, String title) {
        int maxWidth = 0;
        for (String line : content) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }
        if (title != null && title.length() + 4 > maxWidth) {
            maxWidth = title.length() + 4;
        }
        
        // Top border
        println(BORDER_CORNER_TL + BORDER_HORIZONTAL.repeat(maxWidth) + BORDER_CORNER_TR);
        
        // Title (if provided)
        if (title != null && !title.isEmpty()) {
            int padding = (maxWidth - title.length()) / 2;
            println(BORDER_VERTICAL + " ".repeat(padding) + title + " ".repeat(maxWidth - padding - title.length()) + BORDER_VERTICAL);
            println(BORDER_T_JUNCTION + BORDER_HORIZONTAL.repeat(maxWidth) + BORDER_L_JUNCTION);
        }
        
        // Content
        for (String line : content) {
            println(BORDER_VERTICAL + line + " ".repeat(maxWidth - line.length()) + BORDER_VERTICAL);
        }
        
        // Bottom border
        println(BORDER_CORNER_BL + BORDER_HORIZONTAL.repeat(maxWidth) + BORDER_CORNER_BR);
    }

    /**
     * Draws a simple menu.
     */
    public void drawMenu(String title, String[] options) {
        clearScreen();
        
        // Title bar
        int width = Math.max(title.length() + 4, 60);
        println();
        println(BORDER_CORNER_TL + BORDER_HORIZONTAL.repeat(width) + BORDER_CORNER_TR);
        println(BORDER_VERTICAL + " " + title + " ".repeat(width - title.length() - 2) + BORDER_VERTICAL);
        println(BORDER_T_JUNCTION + BORDER_HORIZONTAL.repeat(width) + BORDER_L_JUNCTION);
        
        // Options
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            println(BORDER_VERTICAL + " " + option + " ".repeat(width - option.length() - 2) + BORDER_VERTICAL);
        }
        
        // Bottom border
        println(BORDER_CORNER_BL + BORDER_HORIZONTAL.repeat(width) + BORDER_CORNER_BR);
        println();
    }

    /**
     * Displays the DSF header.
     */
    public void displayHeader(String userId, String cicsName, String roleName) {
        int width = 70;
        println();
        println("╔" + "═".repeat(width) + "╗");
        println("║" + centerText("DSF - Det Sentrale Folketrygdsystemet", width) + "║");
        println("╠" + "═".repeat(width) + "╣");
        
        String infoLine = String.format("CICS: %-12s BRUKER: %-12s DATO: %s", 
            cicsName != null ? cicsName : "RA001",
            userId != null ? userId : "        ",
            DateUtil.getCurrentDateNorwegian().trim());
        println("║" + infoLine + " ".repeat(width - infoLine.length()) + "║");
        
        if (roleName != null && !roleName.isEmpty()) {
            String roleLine = "ROLLE: " + roleName;
            println("║" + roleLine + " ".repeat(width - roleLine.length()) + "║");
        }
        
        println("╚" + "═".repeat(width) + "╝");
        println();
    }

    /**
     * Centers text within a given width.
     */
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    /**
     * Displays a message and waits for acknowledgment.
     */
    public void showMessage(String title, String message) {
        clearScreen();
        drawBox(new String[]{message}, title);
        println();
        pressEnterToContinue();
    }

    /**
     * Displays an error message.
     */
    public void showError(String message) {
        println();
        println("╔" + "═".repeat(50) + "╗");
        println("║" + centerText("FEILMELDING", 50) + "║");
        println("╠" + "═".repeat(50) + "╣");
        println("║ " + message + " ".repeat(48 - message.length()) + "║");
        println("╚" + "═".repeat(50) + "╝");
        println();
    }

    @Override
    public void close() throws IOException {
        terminal.close();
    }
}

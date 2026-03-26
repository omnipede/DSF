package com.nav.dsf.infrastructure.terminal;

import com.nav.dsf.common.util.FNRValidator;

/**
 * Input handler for DSF terminal application.
 * Validates and processes user input similar to CICS map field handling.
 * 
 * This replaces the PL/I EXEC CICS RECEIVE MAP operations.
 */
public class InputHandler {

    /**
     * Reads a non-empty string from user input.
     */
    public static String readString(TerminalUI terminal, String prompt) {
        while (true) {
            String input = terminal.readLine(prompt);
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }
            terminal.showError("Input cannot be empty");
        }
    }

    /**
     * Reads a string with optional validation.
     */
    public static String readString(TerminalUI terminal, String prompt, int maxLength) {
        while (true) {
            String input = terminal.readLine(prompt);
            if (input == null) {
                return null;
            }
            input = input.trim();
            if (input.isEmpty()) {
                return "";
            }
            if (input.length() <= maxLength) {
                return input;
            }
            terminal.showError("Input exceeds maximum length of " + maxLength);
        }
    }

    /**
     * Reads a Norwegian FNR (11 digits).
     */
    public static String readFNR(TerminalUI terminal, String prompt) {
        while (true) {
            String input = terminal.readLine(prompt);
            if (input == null) {
                return null;
            }
            input = input.trim().replaceAll("\\s+", "");
            
            if (input.isEmpty()) {
                return "";
            }
            
            if (input.length() != 11) {
                terminal.showError("FNR must be 11 digits");
                continue;
            }
            
            if (!input.matches("\\d{11}")) {
                terminal.showError("FNR must contain only digits");
                continue;
            }
            
            if (!FNRValidator.isValid(input)) {
                terminal.showError("Invalid FNR checksum");
                continue;
            }
            
            return input;
        }
    }

    /**
     * Reads an integer from user input.
     */
    public static Integer readInteger(TerminalUI terminal, String prompt) {
        while (true) {
            String input = terminal.readLine(prompt);
            if (input == null) {
                return null;
            }
            input = input.trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                terminal.showError("Please enter a valid number");
            }
        }
    }

    /**
     * Reads an integer within a specified range.
     */
    public static Integer readInteger(TerminalUI terminal, String prompt, int min, int max) {
        while (true) {
            Integer value = readInteger(terminal, prompt);
            if (value == null) {
                return null;
            }
            if (value >= min && value <= max) {
                return value;
            }
            terminal.showError("Value must be between " + min + " and " + max);
        }
    }

    /**
     * Reads a single character choice from a set of options.
     */
    public static Character readChoice(TerminalUI terminal, String prompt, char[] validOptions) {
        while (true) {
            String input = terminal.readLine(prompt);
            if (input == null) {
                return null;
            }
            input = input.trim().toUpperCase();
            if (input.isEmpty()) {
                return null;
            }
            char choice = input.charAt(0);
            for (char valid : validOptions) {
                if (choice == valid) {
                    return choice;
                }
            }
            terminal.showError("Invalid choice. Valid options: " + new String(validOptions));
        }
    }

    /**
     * Reads a date in yyyyMMdd format.
     */
    public static String readDate(TerminalUI terminal, String prompt) {
        while (true) {
            String input = terminal.readLine(prompt);
            if (input == null) {
                return null;
            }
            input = input.trim();
            if (input.isEmpty()) {
                return "";
            }
            if (input.length() != 8) {
                terminal.showError("Date must be in yyyyMMdd format (8 digits)");
                continue;
            }
            if (!input.matches("\\d{8}")) {
                terminal.showError("Date must contain only digits");
                continue;
            }
            // Basic date validation
            int year = Integer.parseInt(input.substring(0, 4));
            int month = Integer.parseInt(input.substring(4, 6));
            int day = Integer.parseInt(input.substring(6, 8));
            if (month < 1 || month > 12 || day < 1 || day > 31) {
                terminal.showError("Invalid date");
                continue;
            }
            return input;
        }
    }

    /**
     * Reads a yes/no response.
     */
    public static Boolean readYesNo(TerminalUI terminal, String prompt) {
        while (true) {
            String input = terminal.readLine(prompt + " (J/N)");
            if (input == null) {
                return null;
            }
            input = input.trim().toUpperCase();
            if (input.equals("J") || input.equals("JA") || input.equals("Y") || input.equals("YES")) {
                return true;
            }
            if (input.equals("N") || input.equals("NEI") || input.equals("NO")) {
                return false;
            }
            terminal.showError("Please enter J (Ja/Yes) or N (Nei/No)");
        }
    }

    /**
     * Reads a menu selection.
     */
    public static String readMenuSelection(TerminalUI terminal, String prompt) {
        return readString(terminal, prompt, 10);
    }
}

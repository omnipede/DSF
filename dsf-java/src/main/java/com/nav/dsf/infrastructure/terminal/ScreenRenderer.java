package com.nav.dsf.infrastructure.terminal;

import java.util.List;

/**
 * Screen renderer for DSF terminal application.
 * Generates formatted terminal output similar to CICS BMS maps.
 * 
 * This replaces the PL/I EXEC CICS SEND MAP operations.
 */
public class ScreenRenderer {

    private static final int DEFAULT_WIDTH = 70;

    /**
     * Renders the login screen (equivalent to S001011 map).
     */
    public static void renderLoginScreen(TerminalUI terminal, String cicsName) {
        terminal.clearScreen();
        terminal.displayHeader(null, cicsName, null);
        
        String[] lines = {
            "",
            "  VELKOMMEN TIL DSF",
            "",
            "  TAST INN BRUKER-ID:",
            "",
            "  ┌────────────────────────────────────────────────────┐",
            "  │ BRUKER-ID :                                        │",
            "  └────────────────────────────────────────────────────┘",
            "",
            "  TAST F1 FOR HJELP, F3 FOR AVSLUTT"
        };
        
        for (String line : lines) {
            terminal.println(line);
        }
    }

    /**
     * Renders the role selection screen.
     */
    public static void renderRoleSelection(TerminalUI terminal, String userId, String cicsName,
                                           List<com.nav.dsf.infrastructure.security.Role> roles, List<Integer> tknrs) {
        terminal.clearScreen();
        terminal.displayHeader(userId, cicsName, null);

        terminal.println();
        terminal.println("  ╔══════╦════════════════════════════════════════════════════╦══════╗");
        terminal.println("  ║ VALG ║ ROLLE                                              ║ TKNR ║");
        terminal.println("  ╠══════╬════════════════════════════════════════════════════╬══════╣");

        for (int i = 0; i < Math.min(roles.size(), 16); i++) {
            String roleCode = roles.get(i).getCode();
            String roleDesc = roles.get(i).getDescription();
            // Truncate description if too long (max 42 chars for role name)
            if (roleDesc.length() > 42) {
                roleDesc = roleDesc.substring(0, 39) + "...";
            }
            String role = roleCode + " - " + roleDesc;
            // Truncate if total role string is still too long
            if (role.length() > 54) {
                role = role.substring(0, 51) + "...";
            }
            String tknr = tknrs.size() > i ? String.valueOf(tknrs.get(i)) : "     ";
            String line = String.format("  ║  %2d  ║ %-52s ║ %5s║", i + 1, role, tknr);
            terminal.println(line);
        }

        terminal.println("  ╚══════╩════════════════════════════════════════════════════╩══════╝");
        terminal.println();
        terminal.println("  TAST VALG (1-" + Math.min(roles.size(), 16) + ") ELLER F3 FOR AVSLUTT:");
        terminal.println();
    }

    /**
     * Renders the main function menu (equivalent to S001013 map).
     */
    public static void renderMainMenu(TerminalUI terminal, String userId, String cicsName, String roleName) {
        terminal.clearScreen();
        terminal.displayHeader(userId, cicsName, roleName);
        
        terminal.println("  VELG FUNKSJONSKODE:");
        terminal.println();
        terminal.println("  ┌─────────────────────────────────────────────────────────────┐");
        terminal.println("  │  A - ADMINISTRASJON                                         │");
        terminal.println("  │  F - FORESPØRSEL (Vis person)                               │");
        terminal.println("  │  R - REGISTRERING (Nye/endre blanketter)                    │");
        terminal.println("  │  I - JUSTERING INNTEKTER                                    │");
        terminal.println("  │  V - VENTETRANS-BEHANDLING                                  │");
        terminal.println("  │  X - AVSLUTT                                                │");
        terminal.println("  └─────────────────────────────────────────────────────────────┘");
        terminal.println();
        terminal.println("  TAST KODE: _");
        terminal.println();
    }

    /**
     * Renders the registration menu (equivalent to S001014 map).
     */
    public static void renderRegistrationMenu(TerminalUI terminal, String userId, String cicsName, String roleName) {
        terminal.clearScreen();
        terminal.displayHeader(userId, cicsName, roleName);

        terminal.println("  REGISTRERING - VELG STYREKODE:");
        terminal.println();
        terminal.println("  ┌─────────────────────────────────────────────────────────────┐");
        terminal.println("  │  AP - ALDERSPENSJON            UP - UFØREPENSJON            │");
        terminal.println("  │  EP - ETTERLATT EKTEFELLE      FB - FORELDRELØSE BARN       │");
        terminal.println("  │  BP - ETTERLATTE BARN          FT - FORSØRGINGSTILL.        │");
        terminal.println("  │  TG - TILLEGGSBLANKETT         E1 - ENDRINGSBLANKETT-1      │");
        terminal.println("  │  O1 - OPPHØRSBLANKETT-1        O2 - OPPHØRSBLANKETT-2       │");
        terminal.println("  │  AF - AFP (AVTALEFESTET)       YK - YRKESKADE               │");
        terminal.println("  │  UF - UNGE UFØRE FØR 1967                                   │");
        terminal.println("  │  XX - TILBAKE TIL HOVEDMENY                                 │");
        terminal.println("  └─────────────────────────────────────────────────────────────┘");
        terminal.println();
        terminal.println("  TAST STYREKODE: _");
        terminal.println();
    }

    /**
     * Renders Age Pension (AP) registration screen.
     */
    public static void renderAPRegistration(TerminalUI terminal, String userId, String cicsName, 
                                            String roleName, String fnr, String name) {
        terminal.clearScreen();
        terminal.displayHeader(userId, cicsName, roleName);

        terminal.println();
        terminal.println("  ALDERSPENSJON - REGISTRERING");
        terminal.println();
        terminal.println("  ┌─────────────────────────────────────────────────────────────┐");
        terminal.println("  │  FØDSELSNUMMER : " + (fnr != null ? fnr : "") + 
            " ".repeat(Math.max(0, 48 - (fnr != null ? fnr.length() : 0))) + "│");
        terminal.println("  │  NAVN          : " + (name != null ? name : "") + 
            " ".repeat(Math.max(0, 48 - (name != null ? name.length() : 0))) + "│");
        terminal.println("  │                                                             │");
        terminal.println("  │  TRYGDETID (ÅR): _____                                      │");
        terminal.println("  │  GRUNNPENSJON  : _____                                      │");
        terminal.println("  │  SÆRTEKST    : _____                                      │");
        terminal.println("  │                                                             │");
        terminal.println("  │  [F1=HJELP]  [F3=AVBRYT]  [F12=NESTE]                        │");
        terminal.println("  └─────────────────────────────────────────────────────────────┘");
        terminal.println();
    }

    /**
     * Renders Disability Pension (UP) registration screen.
     */
    public static void renderUPRegistration(TerminalUI terminal, String userId, String cicsName, 
                                            String roleName, String fnr, String name) {
        terminal.clearScreen();
        terminal.displayHeader(userId, cicsName, roleName);

        terminal.println();
        terminal.println("  UFØREPENSJON - REGISTRERING");
        terminal.println();
        terminal.println("  ┌─────────────────────────────────────────────────────────────┐");
        terminal.println("  │  FØDSELSNUMMER : " + (fnr != null ? fnr : "") + 
            " ".repeat(Math.max(0, 48 - (fnr != null ? fnr.length() : 0))) + "│");
        terminal.println("  │  NAVN          : " + (name != null ? name : "") + 
            " ".repeat(Math.max(0, 48 - (name != null ? name.length() : 0))) + "│");
        terminal.println("  │                                                             │");
        terminal.println("  │  UFØRHETSGRAD (%): _____                                    │");
        terminal.println("  │  SYKDOMSKODE     : _____                                    │");
        terminal.println("  │  YRKESKODE       : _____                                    │");
        terminal.println("  │  GRUNNPENSJON    : _____                                    │");
        terminal.println("  │  SÆRTEKST        : _____                                    │");
        terminal.println("  │                                                             │");
        terminal.println("  │  [F1=HJELP]  [F3=AVBRYT]  [F12=NESTE]                        │");
        terminal.println("  └─────────────────────────────────────────────────────────────┘");
        terminal.println();
    }

    /**
     * Renders the inquiry screen (equivalent to S001015 map).
     */
    public static void renderInquiryScreen(TerminalUI terminal, String userId, String cicsName, 
                                           String roleName, String fnr) {
        terminal.clearScreen();
        terminal.displayHeader(userId, cicsName, roleName);
        
        terminal.println("  FORESPØRSEL - PERSONOPPLYSNINGER");
        terminal.println();
        terminal.println("  ┌─────────────────────────────────────────────────────────────┐");
        terminal.println("  │  FØDSELSNUMMER : " + (fnr != null ? fnr : "_______________") + "                          │");
        terminal.println("  └─────────────────────────────────────────────────────────────┘");
        terminal.println();
        terminal.println("  TAST FØDSELSNUMMER (11 SIFFER):");
        terminal.println();
    }

    /**
     * Renders person information display.
     */
    public static void renderPersonInfo(TerminalUI terminal, String userId, String cicsName, 
                                        String roleName, String personDisplay) {
        terminal.clearScreen();
        terminal.displayHeader(userId, cicsName, roleName);
        
        terminal.println(personDisplay);
        terminal.println();
        terminal.println("  TRYKK ENTER FOR TILBAKE");
    }

    /**
     * Renders a generic message screen.
     */
    public static void renderMessageScreen(TerminalUI terminal, String title, String message) {
        terminal.clearScreen();
        
        String[] lines = message.split("\n");
        terminal.drawBox(lines, title);
        terminal.println();
    }

    /**
     * Renders the pause screen (equivalent to S001012 map).
     */
    public static void renderPauseScreen(TerminalUI terminal) {
        terminal.clearScreen();
        
        int width = 50;
        terminal.println();
        terminal.println("╔" + "═".repeat(width) + "╗");
        terminal.println("║" + centerText("DSF - PAUSE", width) + "║");
        terminal.println("╠" + "═".repeat(width) + "╣");
        terminal.println("║" + centerText("Takk for nå", width) + "║");
        terminal.println("╚" + "═".repeat(width) + "╝");
        terminal.println();
    }

    /**
     * Renders the exit screen.
     */
    public static void renderExitScreen(TerminalUI terminal) {
        terminal.clearScreen();
        
        int width = 50;
        terminal.println();
        terminal.println("╔" + "═".repeat(width) + "╗");
        terminal.println("║" + centerText("DSF - AVSLUTTET", width) + "║");
        terminal.println("╠" + "═".repeat(width) + "╣");
        terminal.println("║" + centerText("Systemet er nå avsluttet", width) + "║");
        terminal.println("║" + centerText("Takk for denne gang", width) + "║");
        terminal.println("╚" + "═".repeat(width) + "╝");
        terminal.println();
    }

    /**
     * Centers text within a given width.
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    /**
     * Draws a horizontal separator line.
     */
    public static void drawSeparator(TerminalUI terminal, int width) {
        terminal.println("─".repeat(width));
    }

    /**
     * Renders a form field.
     */
    public static void renderField(TerminalUI terminal, String label, String value, int totalWidth) {
        String line = String.format("  %-20s : %s", label, value != null ? value : "");
        terminal.println(line);
    }

    /**
     * Renders a numeric field with formatting.
     */
    public static void renderNumericField(TerminalUI terminal, String label, Integer value, int totalWidth) {
        String valueStr = value != null ? String.format("%,d", value) : "";
        renderField(terminal, label, valueStr, totalWidth);
    }
}

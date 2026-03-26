package com.nav.dsf.application;

/**
 * Application state enumeration.
 * Represents the current state of the DSF application flow.
 * 
 * Maps to the transaction flow in the original PL/I system:
 * R010 -> R020 -> R030 -> R040/R410/etc.
 */
public enum AppState {
    START("R010", "Start - User ID entry"),
    LOGIN("R010", "Login screen"),
    ROLE_SELECTION("R020", "Role selection"),
    MAIN_MENU("R030", "Main function menu"),
    REGISTRATION_MENU("R040", "Registration menu"),
    INQUIRY("F410", "Inquiry - Person lookup"),
    ADMIN("RA20", "Administration"),
    INCOME_ADJUSTMENT("R0I1", "Income adjustment"),
    WAITING_TRANS("R048", "Waiting transaction handling"),
    EXIT("0000", "Exit");

    private final String transCode;
    private final String description;

    AppState(String transCode, String description) {
        this.transCode = transCode;
        this.description = description;
    }

    public String getTransCode() {
        return transCode;
    }

    public String getDescription() {
        return description;
    }
}

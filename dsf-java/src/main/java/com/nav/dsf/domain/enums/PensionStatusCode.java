package com.nav.dsf.domain.enums;

/**
 * Status codes for pension records.
 * Mapped from the original DSF STATUS_KODE field.
 */
public enum PensionStatusCode {
    ACTIVE(' ', "Active - Pensionist"),
    DECEASED('X', "Deceased or terminated with U/Y/AFP history"),
    TERMINATED('O', "Other terminated persons");

    private final char code;
    private final String description;

    PensionStatusCode(char code, String description) {
        this.code = code;
        this.description = description;
    }

    public char getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PensionStatusCode fromCode(char code) {
        for (PensionStatusCode status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return ACTIVE; // Default
    }
}

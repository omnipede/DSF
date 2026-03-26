package com.nav.dsf.domain.enums;

/**
 * Pension types supported by DSF system.
 * Original Norwegian codes from the PL/I system.
 */
public enum PensionType {
    AP("Alderspensjon", "Age Pension"),
    UP("Uførepensjon", "Disability Pension"),
    EP("Etterlattepensjon ektefelle", "Survivor Spouse Pension"),
    FB("Foreldreløse barn", "Orphaned Children Pension"),
    BP("Etterlatte barn", "Survivor Children Pension"),
    FT("Forsørgingstillegg", "Support Supplement"),
    YK("Yrkesskadepensjon", "Occupational Injury Pension"),
    AF("AFP - Avtalefestet pensjon", "Early Retirement Pension"),
    TG("Tilleggsblankett", "Supplementary Form"),
    E1("Endringsblankett-1", "Change Form 1"),
    O1("Opphørsblankett-1", "Termination Form 1"),
    O2("Opphørsblankett-2", "Termination Form 2"),
    MK("Medisinsk kode", "Medical Code"),
    UF("Unge uføre før 1967", "Young Disabled Before 1967");

    private final String norwegianName;
    private final String englishName;

    PensionType(String norwegianName, String englishName) {
        this.norwegianName = norwegianName;
        this.englishName = englishName;
    }

    public String getNorwegianName() {
        return norwegianName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public static PensionType fromCode(String code) {
        for (PensionType type : values()) {
            if (type.name().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown pension type code: " + code);
    }
}

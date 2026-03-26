package com.nav.dsf.infrastructure.security;

/**
 * User role enumeration.
 * Maps to ACF2 ROLLE field from the original DSF system.
 */
public enum Role {
    AP_SAKSBEHANDLER("AP-SAKSBEHANDLER", "Alderspensjon saksbehandler"),
    UP_SAKSBEHANDLER("UP-SAKSBEHANDLER", "Uførepensjon saksbehandler"),
    EP_SAKSBEHANDLER("EP-SAKSBEHANDLER", "Etterlattepensjon ektefelle saksbehandler"),
    FB_SAKSBEHANDLER("FB-SAKSBEHANDLER", "Foreldreløse barn saksbehandler"),
    BP_SAKSBEHANDLER("BP-SAKSBEHANDLER", "Etterlatte barn saksbehandler"),
    FT_SAKSBEHANDLER("FT-SAKSBEHANDLER", "Forsørgingstillegg saksbehandler"),
    YK_SAKSBEHANDLER("YK-SAKSBEHANDLER", "Yrkesskade saksbehandler"),
    AF_SAKSBEHANDLER("AF-SAKSBEHANDLER", "AFP saksbehandler"),
    SAKSBEHANDLER("SAKSBEHANDLER", "Generell saksbehandler"),
    SENIOR_SAKSBEHANDLER("SENIOR-SAKSBEHANDLER", "Senior saksbehandler"),
    VEILEDER("VEILEDER", "Veileder"),
    SUPERBRUKER("SUPERBRUKER", "Superbruker"),
    SYSTEMANSVARLIG("SYSTEMANSVARLIG", "Systemansvarlig"),
    UTVIKLER("UTVIKLER", "Utvikler"),
    TESTER("TESTER", "Tester");

    private final String code;
    private final String description;

    Role(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Role fromCode(String code) {
        for (Role role : values()) {
            if (role.code.equalsIgnoreCase(code) || role.name().equalsIgnoreCase(code)) {
                return role;
            }
        }
        return null;
    }
}

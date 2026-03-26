package com.nav.dsf.domain.enums;

/**
 * Family relationship types.
 * Mapped from the original DSF TILKNYTNINGSKODE field.
 */
public enum FamilyRelationshipType {
    SPOUSE('S', "Spouse (ektefelle)"),
    CHILD('B', "Child (barn)"),
    PARENT('F', "Parent (forelder)"),
    SIBLING('Ø', "Sibling (søsken)"),
    DECEASED_SPOUSE('A', "Deceased spouse (avdød ektefelle)"),
    DECEASED_CHILD('D', "Deceased child (avdødt barn)"),
    DECEASED_PARENT('G', "Deceased parent (avdød forelder)"),
    FAMILY_CAREGIVER('P', "Family caregiver (familiepleier)");

    private final char code;
    private final String description;

    FamilyRelationshipType(char code, String description) {
        this.code = code;
        this.description = description;
    }

    public char getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static FamilyRelationshipType fromCode(char code) {
        for (FamilyRelationshipType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}

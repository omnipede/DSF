package com.nav.dsf.domain.model;

import java.time.LocalDate;

/**
 * Family member record.
 * Maps to TILKN segment from database.txt (104 bytes total, 8 bytes × 13 members).
 * 
 * Original PL/I structure:
 *   3 TILKN(13) UNALIGNED,
 *    4 FNR_TILKN                DEC FIXED (11),
 *    4 TILKNYTNINGSKODE             CHAR  (1),
 *    4 FT_FØR_91                    CHAR  (1),
 */
public class FamilyMember {
    private String fnr;                    // 11-digit Norwegian national ID
    private String relationshipCode;       // TILKNYTNINGSKODE
    private String ftBefore91;             // FT før 1991

    public FamilyMember() {
    }

    public FamilyMember(String fnr, String relationshipCode) {
        this.fnr = fnr;
        this.relationshipCode = relationshipCode != null ? relationshipCode : " ";
        this.ftBefore91 = " ";
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getRelationshipCode() {
        return relationshipCode;
    }

    public void setRelationshipCode(String relationshipCode) {
        this.relationshipCode = relationshipCode;
    }

    public String getFtBefore91() {
        return ftBefore91;
    }

    public void setFtBefore91(String ftBefore91) {
        this.ftBefore91 = ftBefore91;
    }

    @Override
    public String toString() {
        return String.format("FamilyMember{FNR=%s, relationship=%s}", 
            fnr, relationshipCode);
    }
}

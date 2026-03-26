package com.nav.dsf.domain.model;

import com.nav.dsf.domain.enums.PensionStatusCode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Pension status information.
 * Maps to STATUS segment from database.txt (94 bytes).
 * 
 * Original PL/I structure includes:
 * - VIRK_DATO (9 digits), STATUS_KODE (1), PENSJONSTYPE1-3, SIVILSTAND
 * - TT (Trygdetid) fields, SUM_YTELSE, DØDSDATO, etc.
 */
public class PensionStatus {
    private LocalDate effectiveDate;          // VIRK_DATO_ÅMD
    private PensionStatusCode statusCode;     // STATUS_KODE
    private String pensionType1;              // PENSJONSTYPE1
    private String pensionType2;              // PENSJONSTYPE2
    private String pensionType3;              // PENSJONSTYPE3
    private String civilStatus;               // SIVILSTAND
    
    // Trygdetid (Insurance period)
    private int ttBefore1967;                 // TT_FØR_1967 (years)
    private int ttAfter1966;                  // TT_ETTER_1966 (months)
    private int ttFuture;                     // TT_FRAMT
    private int tt67to70;                     // TT_67_TIL_70
    private int ttAnv;                        // TT_ANV (years)
    
    private String firstTimeRegistered;       // FØRSTE_GANG_REG
    private int benefitAmount;                // SUM_YTELSE
    private int numberOfChildren;             // ANTALL_BARN
    private LocalDate deathDate;              // DØDSDATO_ÅMD
    private String deathFromOccupationalInjury; // DØD_AV_YRKESKADE
    private String condition8_4_3A;           // VILKÅR_8_4_3A
    
    private int ttGuarantee;                  // TT_GARANTI
    private LocalDate gDate;                  // G_DATO_ÅMD
    private LocalDate pointSupplementDate;    // POENGTILLEGG_DATO_ÅMD
    private String pointSupplementCode;       // POENGTILLEGG_KODE
    private String pensionRightBefore91;      // PENSJONSRETT_FØR_91
    private int tt16to66;                     // TT_16_66 (months)
    private String tpGuaranteeCode;           // TP_GAR_KODE
    private String oldCollisionRule;          // GAMMEL_SAMMENSTØTS_REGEL
    
    // Guarantee pension
    private int guaranteeTp;                  // GARANTI_TP
    private LocalDate guaranteeDate;          // GARANTI_DATO_ÅMD
    private int gtLov92;                      // GT_LOV92
    private int gtTilleggLov92;               // GT_TILLEGG_LOV92
    
    private String oldFtCode;                 // GAMMEL_FT_KODE
    private int occupationalCode;             // YRKES_KODE (3 digits)
    private String extraIncomeOver2G;         // EK_INNT_OVER_2G
    private LocalDate freezeDate;             // FRYSDATO_ÅMD
    private String freezeCode;                // FRYSKODE
    private String pensionBefore9802;         // PENSJON_FØR_9802
    private String reduceGp3_2_5;             // RED_GP_3_2_5
    private String gpReductionCode;           // GP_REDUKSJON_KODE
    private LocalDate freeIncomeDate;         // FRIINNTEKT_DATO_ÅMD

    // Gross amounts for calculation display
    private int gpBrutto;                     // GP_BRUTTO
    private int tpBrutto;                     // TP_BRUTTO
    private int stBrutto;                     // ST_BRUTTO
    private int btBrutto;                     // BT_BRUTTO
    private int ktBrutto;                     // KT_BRUTTO
    private int etBrutto;                     // ET_BRUTTO

    public PensionStatus() {
        this.statusCode = PensionStatusCode.ACTIVE;
        this.pensionType1 = " ";
        this.pensionType2 = " ";
        this.pensionType3 = " ";
        this.civilStatus = " ";
    }

    // Getters and Setters
    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public PensionStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(PensionStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getPensionType1() {
        return pensionType1;
    }

    public void setPensionType1(String pensionType1) {
        this.pensionType1 = pensionType1;
    }

    public String getPensionType2() {
        return pensionType2;
    }

    public void setPensionType2(String pensionType2) {
        this.pensionType2 = pensionType2;
    }

    public String getPensionType3() {
        return pensionType3;
    }

    public void setPensionType3(String pensionType3) {
        this.pensionType3 = pensionType3;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public int getTtBefore1967() {
        return ttBefore1967;
    }

    public void setTtBefore1967(int ttBefore1967) {
        this.ttBefore1967 = ttBefore1967;
    }

    public int getTtAfter1966() {
        return ttAfter1966;
    }

    public void setTtAfter1966(int ttAfter1966) {
        this.ttAfter1966 = ttAfter1966;
    }

    public int getTtFuture() {
        return ttFuture;
    }

    public void setTtFuture(int ttFuture) {
        this.ttFuture = ttFuture;
    }

    public int getTt67to70() {
        return tt67to70;
    }

    public void setTt67to70(int tt67to70) {
        this.tt67to70 = tt67to70;
    }

    public int getTtAnv() {
        return ttAnv;
    }

    public void setTtAnv(int ttAnv) {
        this.ttAnv = ttAnv;
    }

    public String getFirstTimeRegistered() {
        return firstTimeRegistered;
    }

    public void setFirstTimeRegistered(String firstTimeRegistered) {
        this.firstTimeRegistered = firstTimeRegistered;
    }

    public int getBenefitAmount() {
        return benefitAmount;
    }

    public void setBenefitAmount(int benefitAmount) {
        this.benefitAmount = benefitAmount;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathFromOccupationalInjury() {
        return deathFromOccupationalInjury;
    }

    public void setDeathFromOccupationalInjury(String deathFromOccupationalInjury) {
        this.deathFromOccupationalInjury = deathFromOccupationalInjury;
    }

    public String getCondition8_4_3A() {
        return condition8_4_3A;
    }

    public void setCondition8_4_3A(String condition8_4_3A) {
        this.condition8_4_3A = condition8_4_3A;
    }

    public int getTtGuarantee() {
        return ttGuarantee;
    }

    public void setTtGuarantee(int ttGuarantee) {
        this.ttGuarantee = ttGuarantee;
    }

    public LocalDate getgDate() {
        return gDate;
    }

    public void setgDate(LocalDate gDate) {
        this.gDate = gDate;
    }

    public LocalDate getPointSupplementDate() {
        return pointSupplementDate;
    }

    public void setPointSupplementDate(LocalDate pointSupplementDate) {
        this.pointSupplementDate = pointSupplementDate;
    }

    public String getPointSupplementCode() {
        return pointSupplementCode;
    }

    public void setPointSupplementCode(String pointSupplementCode) {
        this.pointSupplementCode = pointSupplementCode;
    }

    public String getPensionRightBefore91() {
        return pensionRightBefore91;
    }

    public void setPensionRightBefore91(String pensionRightBefore91) {
        this.pensionRightBefore91 = pensionRightBefore91;
    }

    public int getTt16to66() {
        return tt16to66;
    }

    public void setTt16to66(int tt16to66) {
        this.tt16to66 = tt16to66;
    }

    public String getTpGuaranteeCode() {
        return tpGuaranteeCode;
    }

    public void setTpGuaranteeCode(String tpGuaranteeCode) {
        this.tpGuaranteeCode = tpGuaranteeCode;
    }

    public String getOldCollisionRule() {
        return oldCollisionRule;
    }

    public void setOldCollisionRule(String oldCollisionRule) {
        this.oldCollisionRule = oldCollisionRule;
    }

    public int getGuaranteeTp() {
        return guaranteeTp;
    }

    public void setGuaranteeTp(int guaranteeTp) {
        this.guaranteeTp = guaranteeTp;
    }

    public LocalDate getGuaranteeDate() {
        return guaranteeDate;
    }

    public void setGuaranteeDate(LocalDate guaranteeDate) {
        this.guaranteeDate = guaranteeDate;
    }

    public int getGtLov92() {
        return gtLov92;
    }

    public void setGtLov92(int gtLov92) {
        this.gtLov92 = gtLov92;
    }

    public int getGtTilleggLov92() {
        return gtTilleggLov92;
    }

    public void setGtTilleggLov92(int gtTilleggLov92) {
        this.gtTilleggLov92 = gtTilleggLov92;
    }

    public String getOldFtCode() {
        return oldFtCode;
    }

    public void setOldFtCode(String oldFtCode) {
        this.oldFtCode = oldFtCode;
    }

    public int getOccupationalCode() {
        return occupationalCode;
    }

    public void setOccupationalCode(int occupationalCode) {
        this.occupationalCode = occupationalCode;
    }

    public String getExtraIncomeOver2G() {
        return extraIncomeOver2G;
    }

    public void setExtraIncomeOver2G(String extraIncomeOver2G) {
        this.extraIncomeOver2G = extraIncomeOver2G;
    }

    public LocalDate getFreezeDate() {
        return freezeDate;
    }

    public void setFreezeDate(LocalDate freezeDate) {
        this.freezeDate = freezeDate;
    }

    public String getFreezeCode() {
        return freezeCode;
    }

    public void setFreezeCode(String freezeCode) {
        this.freezeCode = freezeCode;
    }

    public String getPensionBefore9802() {
        return pensionBefore9802;
    }

    public void setPensionBefore9802(String pensionBefore9802) {
        this.pensionBefore9802 = pensionBefore9802;
    }

    public String getReduceGp3_2_5() {
        return reduceGp3_2_5;
    }

    public void setReduceGp3_2_5(String reduceGp3_2_5) {
        this.reduceGp3_2_5 = reduceGp3_2_5;
    }

    public String getGpReductionCode() {
        return gpReductionCode;
    }

    public void setGpReductionCode(String gpReductionCode) {
        this.gpReductionCode = gpReductionCode;
    }

    public LocalDate getFreeIncomeDate() {
        return freeIncomeDate;
    }

    public void setFreeIncomeDate(LocalDate freeIncomeDate) {
        this.freeIncomeDate = freeIncomeDate;
    }

    public int getGpBrutto() {
        return gpBrutto;
    }

    public void setGpBrutto(int gpBrutto) {
        this.gpBrutto = gpBrutto;
    }

    public int getTpBrutto() {
        return tpBrutto;
    }

    public void setTpBrutto(int tpBrutto) {
        this.tpBrutto = tpBrutto;
    }

    public int getStBrutto() {
        return stBrutto;
    }

    public void setStBrutto(int stBrutto) {
        this.stBrutto = stBrutto;
    }

    public int getBtBrutto() {
        return btBrutto;
    }

    public void setBtBrutto(int btBrutto) {
        this.btBrutto = btBrutto;
    }

    public int getKtBrutto() {
        return ktBrutto;
    }

    public void setKtBrutto(int ktBrutto) {
        this.ktBrutto = ktBrutto;
    }

    public int getEtBrutto() {
        return etBrutto;
    }

    public void setEtBrutto(int etBrutto) {
        this.etBrutto = etBrutto;
    }

    @Override
    public String toString() {
        return String.format("PensionStatus{statusCode=%s, pensionTypes=[%s,%s,%s], TT=%d/%d}",
            statusCode, pensionType1, pensionType2, pensionType3,
            ttBefore1967, ttAfter1966);
    }
}

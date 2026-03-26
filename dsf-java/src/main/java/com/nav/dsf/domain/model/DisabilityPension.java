package com.nav.dsf.domain.model;

/**
 * Disability Pension (Uførepensjon) information.
 * Maps to UFØRPENS segment from database.txt (70 bytes).
 *
 * Original PL/I structure includes:
 * - GP (Grunnpensjon), TP (Tilleggspensjon)
 * - UFØRHETSGRAD (disability percentage)
 * - DIAGNOSEKODE (disease code)
 * - YRKESKODE (occupation code)
 * - VIRK_DATO (effective date)
 */
public class DisabilityPension {
    private int gp;                    // GRUNNPENSJON
    private int tp;                    // TILLEGGSPENSJON
    private int uforhetsgrad;          // UFØRHETSGRAD (0-100%)
    private String diagnosekode;       // DIAGNOSEKODE
    private String yrkeskode;          // YRKESKODE (4 digits)
    private String virkDato;           // VIRK_DATO (YYYYMMDD)
    private String reaktiveringsdato;  // REAKTIVERINGSDATO
    private String opphorsdato;        // OPPHØRSDATO

    public DisabilityPension() {
        this.uforhetsgrad = 0;
        this.gp = 0;
        this.tp = 0;
        this.diagnosekode = " ";
        this.yrkeskode = "0000";
    }

    // Getters and Setters
    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public int getUforhetsgrad() {
        return uforhetsgrad;
    }

    public void setUforhetsgrad(int uforhetsgrad) {
        this.uforhetsgrad = uforhetsgrad;
    }

    public String getDiagnosekode() {
        return diagnosekode;
    }

    public void setDiagnosekode(String diagnosekode) {
        this.diagnosekode = diagnosekode;
    }

    public String getYrkeskode() {
        return yrkeskode;
    }

    public void setYrkeskode(String yrkeskode) {
        this.yrkeskode = yrkeskode;
    }

    public String getVirkDato() {
        return virkDato;
    }

    public void setVirkDato(String virkDato) {
        this.virkDato = virkDato;
    }

    public String getReaktiveringsdato() {
        return reaktiveringsdato;
    }

    public void setReaktiveringsdato(String reaktiveringsdato) {
        this.reaktiveringsdato = reaktiveringsdato;
    }

    public String getOpphorsdato() {
        return opphorsdato;
    }

    public void setOpphorsdato(String opphorsdato) {
        this.opphorsdato = opphorsdato;
    }

    @Override
    public String toString() {
        return String.format("DisabilityPension{GP=%d, TP=%d, UFØRHETSGRAD=%d%%}",
            gp, tp, uforhetsgrad);
    }
}

package com.nav.dsf.domain.model;

/**
 * Age pension information (Alderspensjon - AP).
 * Maps to ALDERSP segment from database.txt (107 bytes).
 * 
 * Original PL/I structure includes:
 * - GP (Grunnpoeng), ST, BT, KT, ET, TP (various pension components)
 * - SPT, OPT (special/ordinary supplement points)
 * - PÅ (reduction factor), APD (age pension percentage)
 * - FAI (actual income), UTTAKSDATO (withdrawal date)
 * - VENTEFAKTOR (waiting factor), etc.
 */
public class AgePension {
    private int gp;                 // Grunnpoeng (Base points)
    private int st;                 // Special supplement
    private int bt;                 // Child supplement
    private int kt;                 // Spouse supplement
    private int et;                 // Additional supplement
    private int tp;                 // Tilleggspoeng (Supplementary points)
    private double spt;             // Special point percentage (3,2)
    private double opt;             // Ordinary point percentage (3,2)
    private int pa;                 // Reduction factor (3 digits)
    private int apd;                // Age pension percentage (3 digits)
    private int fai;                // Actual income (FAI)
    private int faiDate;            // FAI date (9 digits, ÅÅÅÅMMDD format)
    private String p67Code;         // P67 code (1 char)
    private String convPCode;       // Conversion P code (1 char)
    private int convGrad;           // Conversion grade (3 digits)
    private int withdrawalDate;     // UTTAKSDATO (9 digits)
    private int withdrawalApd;      // UTTAKS_APD (3 digits)
    private int ttVent;             // TT_VENT (3 digits)
    private double venteFaktor;     // Waiting factor (5,2)
    private double vtSpt;           // VT special point (3,2)
    private double vtOpt;           // VT ordinary point (3,2)
    private int vtPa;               // VT reduction (3 digits)
    private int gpP67;              // GP P67
    private int tpP67;              // TP P67
    private int vtGp;               // VT GP
    private int vtTp;               // VT TP
    private double spt1291;         // SPT after 1991 (3,2)
    private double opt1291;         // OPT after 1991 (3,2)
    private int pa1291;             // PÅ after 1991 (3 digits)
    private int paAfter91;          // PÅ after 1991 (3 digits)
    private int apTpNetto;          // AP TP net (5 digits)
    private int apGpNetto;          // AP GP net (5 digits)
    private int afpTillegg;         // AFP supplement (5 digits)
    private double afpBup;          // AFP BUP (3,2)
    private int afpTilleggNetto;    // AFP supplement net (5 digits)
    private int apStNetto;          // AP ST net (5 digits)
    private int apEtNetto;          // AP ET net (5 digits)
    private double apTeiFaktor;     // AP TEI factor (5,2)
    private String beregnAltAey;    // Calculate alt AEY (1 char)

    public AgePension() {
        this.p67Code = " ";
        this.convPCode = " ";
        this.beregnAltAey = " ";
    }

    // Getters and Setters
    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public int getBt() {
        return bt;
    }

    public void setBt(int bt) {
        this.bt = bt;
    }

    public int getKt() {
        return kt;
    }

    public void setKt(int kt) {
        this.kt = kt;
    }

    public int getEt() {
        return et;
    }

    public void setEt(int et) {
        this.et = et;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public double getSpt() {
        return spt;
    }

    public void setSpt(double spt) {
        this.spt = spt;
    }

    public double getOpt() {
        return opt;
    }

    public void setOpt(double opt) {
        this.opt = opt;
    }

    public int getPa() {
        return pa;
    }

    public void setPa(int pa) {
        this.pa = pa;
    }

    public int getApd() {
        return apd;
    }

    public void setApd(int apd) {
        this.apd = apd;
    }

    public int getFai() {
        return fai;
    }

    public void setFai(int fai) {
        this.fai = fai;
    }

    public int getFaiDate() {
        return faiDate;
    }

    public void setFaiDate(int faiDate) {
        this.faiDate = faiDate;
    }

    public String getP67Code() {
        return p67Code;
    }

    public void setP67Code(String p67Code) {
        this.p67Code = p67Code;
    }

    public String getConvPCode() {
        return convPCode;
    }

    public void setConvPCode(String convPCode) {
        this.convPCode = convPCode;
    }

    public int getConvGrad() {
        return convGrad;
    }

    public void setConvGrad(int convGrad) {
        this.convGrad = convGrad;
    }

    public int getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(int withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public int getWithdrawalApd() {
        return withdrawalApd;
    }

    public void setWithdrawalApd(int withdrawalApd) {
        this.withdrawalApd = withdrawalApd;
    }

    public int getTtVent() {
        return ttVent;
    }

    public void setTtVent(int ttVent) {
        this.ttVent = ttVent;
    }

    public double getVenteFaktor() {
        return venteFaktor;
    }

    public void setVenteFaktor(double venteFaktor) {
        this.venteFaktor = venteFaktor;
    }

    public double getVtSpt() {
        return vtSpt;
    }

    public void setVtSpt(double vtSpt) {
        this.vtSpt = vtSpt;
    }

    public double getVtOpt() {
        return vtOpt;
    }

    public void setVtOpt(double vtOpt) {
        this.vtOpt = vtOpt;
    }

    public int getVtPa() {
        return vtPa;
    }

    public void setVtPa(int vtPa) {
        this.vtPa = vtPa;
    }

    public int getGpP67() {
        return gpP67;
    }

    public void setGpP67(int gpP67) {
        this.gpP67 = gpP67;
    }

    public int getTpP67() {
        return tpP67;
    }

    public void setTpP67(int tpP67) {
        this.tpP67 = tpP67;
    }

    public int getVtGp() {
        return vtGp;
    }

    public void setVtGp(int vtGp) {
        this.vtGp = vtGp;
    }

    public int getVtTp() {
        return vtTp;
    }

    public void setVtTp(int vtTp) {
        this.vtTp = vtTp;
    }

    public double getSpt1291() {
        return spt1291;
    }

    public void setSpt1291(double spt1291) {
        this.spt1291 = spt1291;
    }

    public double getOpt1291() {
        return opt1291;
    }

    public void setOpt1291(double opt1291) {
        this.opt1291 = opt1291;
    }

    public int getPa1291() {
        return pa1291;
    }

    public void setPa1291(int pa1291) {
        this.pa1291 = pa1291;
    }

    public int getPaAfter91() {
        return paAfter91;
    }

    public void setPaAfter91(int paAfter91) {
        this.paAfter91 = paAfter91;
    }

    public int getApTpNetto() {
        return apTpNetto;
    }

    public void setApTpNetto(int apTpNetto) {
        this.apTpNetto = apTpNetto;
    }

    public int getApGpNetto() {
        return apGpNetto;
    }

    public void setApGpNetto(int apGpNetto) {
        this.apGpNetto = apGpNetto;
    }

    public int getAfpTillegg() {
        return afpTillegg;
    }

    public void setAfpTillegg(int afpTillegg) {
        this.afpTillegg = afpTillegg;
    }

    public double getAfpBup() {
        return afpBup;
    }

    public void setAfpBup(double afpBup) {
        this.afpBup = afpBup;
    }

    public int getAfpTilleggNetto() {
        return afpTilleggNetto;
    }

    public void setAfpTilleggNetto(int afpTilleggNetto) {
        this.afpTilleggNetto = afpTilleggNetto;
    }

    public int getApStNetto() {
        return apStNetto;
    }

    public void setApStNetto(int apStNetto) {
        this.apStNetto = apStNetto;
    }

    public int getApEtNetto() {
        return apEtNetto;
    }

    public void setApEtNetto(int apEtNetto) {
        this.apEtNetto = apEtNetto;
    }

    public double getApTeiFaktor() {
        return apTeiFaktor;
    }

    public void setApTeiFaktor(double apTeiFaktor) {
        this.apTeiFaktor = apTeiFaktor;
    }

    public String getBeregnAltAey() {
        return beregnAltAey;
    }

    public void setBeregnAltAey(String beregnAltAey) {
        this.beregnAltAey = beregnAltAey;
    }

    @Override
    public String toString() {
        return String.format("AgePension{GP=%d, TP=%d, ST=%d, BT=%d, KT=%d, ET=%d, SPT=%.2f, OPT=%.2f}",
            gp, tp, st, bt, kt, et, spt, opt);
    }
}

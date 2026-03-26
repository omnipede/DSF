package com.nav.dsf.domain.model;

/**
 * Early retirement settlement (AFP - Avtalefestet pensjon).
 * Maps to EOAFP segment from database.txt (671 bytes = 61 bytes x 11).
 */
public class EarlyRetirementSettlement {
    private int incomeYear;
    private int pensionGivingIncome;
    private int incomeBeforeAfp;
    private String ifuFlag;
    private int incomeAfterEnd;
    private String ieoFlag;
    private int actualIncomePeriod;
    private String iiapFlag;
    private int oppgFaiPeriod;
    private int teiIap;
    private int fullAfpPeriod;
    private int prevPaidPeriod;
    private int diffOverpaid;
    private int diffUnderpaid;

    public EarlyRetirementSettlement() {
        this.ifuFlag = " ";
        this.ieoFlag = " ";
        this.iiapFlag = " ";
    }

    public int getIncomeYear() {
        return incomeYear;
    }

    public void setIncomeYear(int incomeYear) {
        this.incomeYear = incomeYear;
    }

    public int getPensionGivingIncome() {
        return pensionGivingIncome;
    }

    public void setPensionGivingIncome(int pensionGivingIncome) {
        this.pensionGivingIncome = pensionGivingIncome;
    }

    public int getIncomeBeforeAfp() {
        return incomeBeforeAfp;
    }

    public void setIncomeBeforeAfp(int incomeBeforeAfp) {
        this.incomeBeforeAfp = incomeBeforeAfp;
    }

    public String getIfuFlag() {
        return ifuFlag;
    }

    public void setIfuFlag(String ifuFlag) {
        this.ifuFlag = ifuFlag;
    }

    public int getIncomeAfterEnd() {
        return incomeAfterEnd;
    }

    public void setIncomeAfterEnd(int incomeAfterEnd) {
        this.incomeAfterEnd = incomeAfterEnd;
    }

    public String getIeoFlag() {
        return ieoFlag;
    }

    public void setIeoFlag(String ieoFlag) {
        this.ieoFlag = ieoFlag;
    }

    public int getActualIncomePeriod() {
        return actualIncomePeriod;
    }

    public void setActualIncomePeriod(int actualIncomePeriod) {
        this.actualIncomePeriod = actualIncomePeriod;
    }

    public String getIiapFlag() {
        return iiapFlag;
    }

    public void setIiapFlag(String iiapFlag) {
        this.iiapFlag = iiapFlag;
    }

    public int getOppgFaiPeriod() {
        return oppgFaiPeriod;
    }

    public void setOppgFaiPeriod(int oppgFaiPeriod) {
        this.oppgFaiPeriod = oppgFaiPeriod;
    }

    public int getTeiIap() {
        return teiIap;
    }

    public void setTeiIap(int teiIap) {
        this.teiIap = teiIap;
    }

    public int getFullAfpPeriod() {
        return fullAfpPeriod;
    }

    public void setFullAfpPeriod(int fullAfpPeriod) {
        this.fullAfpPeriod = fullAfpPeriod;
    }

    public int getPrevPaidPeriod() {
        return prevPaidPeriod;
    }

    public void setPrevPaidPeriod(int prevPaidPeriod) {
        this.prevPaidPeriod = prevPaidPeriod;
    }

    public int getDiffOverpaid() {
        return diffOverpaid;
    }

    public void setDiffOverpaid(int diffOverpaid) {
        this.diffOverpaid = diffOverpaid;
    }

    public int getDiffUnderpaid() {
        return diffUnderpaid;
    }

    public void setDiffUnderpaid(int diffUnderpaid) {
        this.diffUnderpaid = diffUnderpaid;
    }

    @Override
    public String toString() {
        return String.format("EarlyRetirementSettlement{year=%d, PGI=%d, IFU=%d}",
            incomeYear, pensionGivingIncome, incomeBeforeAfp);
    }
}

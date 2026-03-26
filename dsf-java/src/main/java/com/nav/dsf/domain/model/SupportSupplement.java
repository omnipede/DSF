package com.nav.dsf.domain.model;

/**
 * Support supplement information (Forsorgingstillegg - FT).
 * Maps to FORSI segment from database.txt (34 bytes).
 */
public class SupportSupplement {
    private int guaranteeSupplementSpouse;
    private int guaranteeSupplementChild;
    private String calculateSupplement;
    private int workIncome;
    private int pensionIncome;
    private int workIncomeSpouse;
    private int pensionIncomeSpouse;
    private int specialChildAmount;
    private int guaranteeSupplementSpecial;

    public SupportSupplement() {
        this.calculateSupplement = " ";
    }

    public int getGuaranteeSupplementSpouse() {
        return guaranteeSupplementSpouse;
    }

    public void setGuaranteeSupplementSpouse(int guaranteeSupplementSpouse) {
        this.guaranteeSupplementSpouse = guaranteeSupplementSpouse;
    }

    public int getGuaranteeSupplementChild() {
        return guaranteeSupplementChild;
    }

    public void setGuaranteeSupplementChild(int guaranteeSupplementChild) {
        this.guaranteeSupplementChild = guaranteeSupplementChild;
    }

    public String getCalculateSupplement() {
        return calculateSupplement;
    }

    public void setCalculateSupplement(String calculateSupplement) {
        this.calculateSupplement = calculateSupplement;
    }

    public int getWorkIncome() {
        return workIncome;
    }

    public void setWorkIncome(int workIncome) {
        this.workIncome = workIncome;
    }

    public int getPensionIncome() {
        return pensionIncome;
    }

    public void setPensionIncome(int pensionIncome) {
        this.pensionIncome = pensionIncome;
    }

    public int getWorkIncomeSpouse() {
        return workIncomeSpouse;
    }

    public void setWorkIncomeSpouse(int workIncomeSpouse) {
        this.workIncomeSpouse = workIncomeSpouse;
    }

    public int getPensionIncomeSpouse() {
        return pensionIncomeSpouse;
    }

    public void setPensionIncomeSpouse(int pensionIncomeSpouse) {
        this.pensionIncomeSpouse = pensionIncomeSpouse;
    }

    public int getSpecialChildAmount() {
        return specialChildAmount;
    }

    public void setSpecialChildAmount(int specialChildAmount) {
        this.specialChildAmount = specialChildAmount;
    }

    public int getGuaranteeSupplementSpecial() {
        return guaranteeSupplementSpecial;
    }

    public void setGuaranteeSupplementSpecial(int guaranteeSupplementSpecial) {
        this.guaranteeSupplementSpecial = guaranteeSupplementSpecial;
    }

    @Override
    public String toString() {
        return String.format("SupportSupplement{calculate=%s, workIncome=%d, pensionIncome=%d}",
            calculateSupplement, workIncome, pensionIncome);
    }
}

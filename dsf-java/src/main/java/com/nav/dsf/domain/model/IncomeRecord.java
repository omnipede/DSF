package com.nav.dsf.domain.model;

/**
 * Income record for a specific year.
 * Maps to PINNTEKT segment from database.txt (264 bytes total, 44 bytes x 6 years).
 * 
 * Original PL/I structure:
 *   3 PINNTEKT(1967:2010) UNALIGNED,
 *    4 PI                       DEC FIXED (9),
 *    4 PI_KODE                      CHAR  (1),
 */
public class IncomeRecord {
    private int year;
    private int pensionGivingIncome;
    private String incomeCode;

    public IncomeRecord() {
    }

    public IncomeRecord(int year, int pensionGivingIncome, String incomeCode) {
        this.year = year;
        this.pensionGivingIncome = pensionGivingIncome;
        this.incomeCode = incomeCode != null ? incomeCode : " ";
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPensionGivingIncome() {
        return pensionGivingIncome;
    }

    public void setPensionGivingIncome(int pensionGivingIncome) {
        this.pensionGivingIncome = pensionGivingIncome;
    }

    public String getIncomeCode() {
        return incomeCode;
    }

    public void setIncomeCode(String incomeCode) {
        this.incomeCode = incomeCode;
    }

    @Override
    public String toString() {
        return String.format("IncomeRecord{year=%d, PI=%d, code='%s'}", 
            year, pensionGivingIncome, incomeCode);
    }
}

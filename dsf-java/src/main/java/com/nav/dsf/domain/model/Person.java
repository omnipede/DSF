package com.nav.dsf.domain.model;

/**
 * Main Person record - the core entity in DSF.
 * Maps to PERSON segment from database.txt (4,472 bytes per person).
 * 
 * This is the main aggregate that contains all pension-related information
 * for a single person in the Norwegian National Insurance System.
 */
public class Person {
    // Identification (58 bytes)
    private String fnr;                      // 11-digit Norwegian national ID (FØDSELSNUMMER)
    private String name;                     // NAVN (25 chars)
    private int tknr;                        // Trygdekontor number (5 digits)
    private String language;                 // SPRÅK (1 char)
    private int ai67;                        // AI67 (5 digits)
    private String blocked;                  // SPERRE (1 char)
    private int blockedDate;                 // SB_DATO_ÅMD (9 digits)
    private String pi66_65;                  // PI_66_65 (1 char)
    private String eosGuarantee;             // EØS_GARANTI (1 char) - A,B,C
    private String tsoId;                    // BRUKER_ID (8 chars) - TSO ID
    private String personCode;               // PERSN_KODE (1 char) - S/I for screened persons
    private String file;                     // FIL (3 chars) - TKNR for blocking

    // Income history 1967-2010 (264 bytes = 44 bytes × 6 years displayed)
    private IncomeRecord[] incomeHistory;    // PINNTEKT array

    // Pre-1967 service and income (28 bytes)
    private int[] militaryServiceYears;      // VERNEPLIKTÅR (4 years)
    private int ai63, ai64, ai65, ai66;      // Income before law enactment
    private int pi66;                        // Pension income 1966

    // Early retirement settlement (671 bytes = 61 bytes × 11)
    private EarlyRetirementSettlement[] afpSettlements;

    // Pension status (94 bytes)
    private PensionStatus pensionStatus;

    // Family members (104 bytes = 8 bytes × 13)
    private FamilyMember[] familyMembers;

    // Support supplement (34 bytes)
    private SupportSupplement supportSupplement;

    // Age pension (107 bytes)
    private AgePension agePension;

    // Disability pension (70 bytes)
    private DisabilityPension disabilityPension;

    // Disability history (266 bytes = 38 bytes × 7) - TODO
    private Object[] disabilityHistory;

    // Disability grade changes (1680 bytes = 140 bytes × 12 × 7) - TODO
    private Object[][] disabilityGradeChanges;

    // Survivor spouse pension (62 bytes) - TODO
    private Object survivorSpousePension;

    // Survivor children pension (50 bytes) - TODO
    private Object survivorChildrenPension;

    // Occupational injury pension (38 bytes) - TODO
    private Object occupationalInjuryPension;

    // Occupational injury history (300 bytes = 30 bytes × 10) - TODO
    private Object[] occupationalInjuryHistory;

    // Occupational injury grade changes (250 bytes = 50 bytes × 5) - TODO
    private Object[] occupationalInjuryGradeChanges;

    // EEA information (130 bytes) - TODO
    private Object eosInfo;

    // Special information (56 bytes) - TODO
    private Object specialInfo;

    public Person() {
        this.fnr = "";
        this.name = "";
        this.language = " ";
        this.blocked = " ";
        this.eosGuarantee = " ";
        this.tsoId = "";
        this.personCode = " ";
        this.file = "   ";
        
        // Initialize arrays with default sizes
        this.incomeHistory = new IncomeRecord[44]; // 1967-2010
        this.militaryServiceYears = new int[4];
        this.afpSettlements = new EarlyRetirementSettlement[11];
        this.familyMembers = new FamilyMember[13];
        this.disabilityHistory = new Object[7];
        this.occupationalInjuryHistory = new Object[10];
    }

    public Person(String fnr, String name) {
        this();
        this.fnr = fnr;
        this.name = name;
        this.pensionStatus = new PensionStatus();
        this.agePension = new AgePension();
        this.disabilityPension = new DisabilityPension();
        this.supportSupplement = new SupportSupplement();
    }

    // Getters and Setters for main fields
    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTknr() {
        return tknr;
    }

    public void setTknr(int tknr) {
        this.tknr = tknr;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getAi67() {
        return ai67;
    }

    public void setAi67(int ai67) {
        this.ai67 = ai67;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public int getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(int blockedDate) {
        this.blockedDate = blockedDate;
    }

    public String getPi66_65() {
        return pi66_65;
    }

    public void setPi66_65(String pi66_65) {
        this.pi66_65 = pi66_65;
    }

    public String getEosGuarantee() {
        return eosGuarantee;
    }

    public void setEosGuarantee(String eosGuarantee) {
        this.eosGuarantee = eosGuarantee;
    }

    public String getTsoId() {
        return tsoId;
    }

    public void setTsoId(String tsoId) {
        this.tsoId = tsoId;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public IncomeRecord[] getIncomeHistory() {
        return incomeHistory;
    }

    public void setIncomeHistory(IncomeRecord[] incomeHistory) {
        this.incomeHistory = incomeHistory;
    }

    public int[] getMilitaryServiceYears() {
        return militaryServiceYears;
    }

    public void setMilitaryServiceYears(int[] militaryServiceYears) {
        this.militaryServiceYears = militaryServiceYears;
    }

    public int getAi63() {
        return ai63;
    }

    public void setAi63(int ai63) {
        this.ai63 = ai63;
    }

    public int getAi64() {
        return ai64;
    }

    public void setAi64(int ai64) {
        this.ai64 = ai64;
    }

    public int getAi65() {
        return ai65;
    }

    public void setAi65(int ai65) {
        this.ai65 = ai65;
    }

    public int getAi66() {
        return ai66;
    }

    public void setAi66(int ai66) {
        this.ai66 = ai66;
    }

    public int getPi66() {
        return pi66;
    }

    public void setPi66(int pi66) {
        this.pi66 = pi66;
    }

    public EarlyRetirementSettlement[] getAfpSettlements() {
        return afpSettlements;
    }

    public void setAfpSettlements(EarlyRetirementSettlement[] afpSettlements) {
        this.afpSettlements = afpSettlements;
    }

    public PensionStatus getPensionStatus() {
        return pensionStatus;
    }

    public void setPensionStatus(PensionStatus pensionStatus) {
        this.pensionStatus = pensionStatus;
    }

    public FamilyMember[] getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(FamilyMember[] familyMembers) {
        this.familyMembers = familyMembers;
    }

    public SupportSupplement getSupportSupplement() {
        return supportSupplement;
    }

    public void setSupportSupplement(SupportSupplement supportSupplement) {
        this.supportSupplement = supportSupplement;
    }

    public AgePension getAgePension() {
        return agePension;
    }

    public void setAgePension(AgePension agePension) {
        this.agePension = agePension;
    }

    public DisabilityPension getDisabilityPension() {
        return disabilityPension;
    }

    public void setDisabilityPension(DisabilityPension disabilityPension) {
        this.disabilityPension = disabilityPension;
    }

    public Object[] getDisabilityHistory() {
        return disabilityHistory;
    }

    public void setDisabilityHistory(Object[] disabilityHistory) {
        this.disabilityHistory = disabilityHistory;
    }

    public Object[][] getDisabilityGradeChanges() {
        return disabilityGradeChanges;
    }

    public void setDisabilityGradeChanges(Object[][] disabilityGradeChanges) {
        this.disabilityGradeChanges = disabilityGradeChanges;
    }

    public Object getSurvivorSpousePension() {
        return survivorSpousePension;
    }

    public void setSurvivorSpousePension(Object survivorSpousePension) {
        this.survivorSpousePension = survivorSpousePension;
    }

    public Object getSurvivorChildrenPension() {
        return survivorChildrenPension;
    }

    public void setSurvivorChildrenPension(Object survivorChildrenPension) {
        this.survivorChildrenPension = survivorChildrenPension;
    }

    public Object getOccupationalInjuryPension() {
        return occupationalInjuryPension;
    }

    public void setOccupationalInjuryPension(Object occupationalInjuryPension) {
        this.occupationalInjuryPension = occupationalInjuryPension;
    }

    public Object[] getOccupationalInjuryHistory() {
        return occupationalInjuryHistory;
    }

    public void setOccupationalInjuryHistory(Object[] occupationalInjuryHistory) {
        this.occupationalInjuryHistory = occupationalInjuryHistory;
    }

    public Object[] getOccupationalInjuryGradeChanges() {
        return occupationalInjuryGradeChanges;
    }

    public void setOccupationalInjuryGradeChanges(Object[] occupationalInjuryGradeChanges) {
        this.occupationalInjuryGradeChanges = occupationalInjuryGradeChanges;
    }

    public Object getEosInfo() {
        return eosInfo;
    }

    public void setEosInfo(Object eosInfo) {
        this.eosInfo = eosInfo;
    }

    public Object getSpecialInfo() {
        return specialInfo;
    }

    public void setSpecialInfo(Object specialInfo) {
        this.specialInfo = specialInfo;
    }

    @Override
    public String toString() {
        return String.format("Person{FNR=%s, Name=%s, TKNR=%d, Language=%s}",
            fnr, name, tknr, language);
    }

    /**
     * Get formatted display string for terminal UI.
     */
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append("═".repeat(60)).append("\n");
        sb.append(" PERSONINFORMASJON\n");
        sb.append("─".repeat(60)).append("\n");
        sb.append(String.format(" FØDSELSNUMMER : %s\n", fnr));
        sb.append(String.format(" NAVN          : %s\n", name));
        sb.append(String.format(" TRYGDEKONTOR  : %05d\n", tknr));
        sb.append(String.format(" SPRÅK         : %s\n", language));
        sb.append(String.format(" SPERRE        : %s\n", blocked));
        
        if (pensionStatus != null) {
            sb.append("─".repeat(60)).append("\n");
            sb.append(" PENSJONSSTATUS\n");
            sb.append(String.format(" STATUS        : %s\n", pensionStatus.getStatusCode()));
            sb.append(String.format(" PENSJONSTYPE  : %s/%s/%s\n", 
                pensionStatus.getPensionType1(),
                pensionStatus.getPensionType2(),
                pensionStatus.getPensionType3()));
        }
        
        if (agePension != null) {
            sb.append("─".repeat(60)).append("\n");
            sb.append(" ALDERSPENSJON\n");
            sb.append(String.format(" GRUNNPOENG    : %d\n", agePension.getGp()));
            sb.append(String.format(" TILLEGGSPOENG : %d\n", agePension.getTp()));
        }
        
        sb.append("═".repeat(60));
        return sb.toString();
    }
}

package com.nav.dsf.domain.service;

import com.nav.dsf.domain.model.AgePension;
import com.nav.dsf.domain.model.PensionStatus;
import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.enums.PensionType;
import com.nav.dsf.domain.enums.PensionStatusCode;

/**
 * Service for Age Pension (Alderspensjon) calculations and processing.
 * Implements business logic from R0010501 and related programs.
 * 
 * References original PL/I programs:
 * - R0010501: AP main control
 * - R0010502-R0010510: AP sub-programs
 * - R0019905: Age calculation
 * - R0019909: Trygdetid (national insurance period) calculation
 */
public class AgePensionService {

    private final ValidationService validationService;

    public AgePensionService(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * AP Registration result.
     */
    public static class APRegistrationResult {
        private final boolean success;
        private final Person person;
        private final String errorMessage;

        public APRegistrationResult(boolean success, Person person, String errorMessage) {
            this.success = success;
            this.person = person;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() { return success; }
        public Person getPerson() { return person; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * AP Registration data.
     */
    public static class APRegistrationData {
        private String fnr;
        private String name;
        private int trygdetid; // Trygdetid in years
        private int grunnpensjon; // Basic pension amount
        private String saertekst; // Special text

        public APRegistrationData() {}

        public APRegistrationData(String fnr, String name, int trygdetid, int grunnpensjon, String saertekst) {
            this.fnr = fnr;
            this.name = name;
            this.trygdetid = trygdetid;
            this.grunnpensjon = grunnpensjon;
            this.saertekst = saertekst;
        }

        // Getters and Setters
        public String getFnr() { return fnr; }
        public void setFnr(String fnr) { this.fnr = fnr; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getTrygdetid() { return trygdetid; }
        public void setTrygdetid(int trygdetid) { this.trygdetid = trygdetid; }
        public int getGrunnpensjon() { return grunnpensjon; }
        public void setGrunnpensjon(int grunnpensjon) { this.grunnpensjon = grunnpensjon; }
        public String getSaertekst() { return saertekst; }
        public void setSaertekst(String saertekst) { this.saertekst = saertekst; }
    }

    /**
     * Registers Age Pension for a person.
     * 
     * @param data Registration data
     * @param person Person record (existing or new)
     * @return Registration result
     */
    public APRegistrationResult registerAgePension(APRegistrationData data, Person person) {
        // Validate input data
        String validationError = validateAPData(data);
        if (validationError != null) {
            return new APRegistrationResult(false, null, validationError);
        }

        // Create new person if not provided
        if (person == null) {
            person = new Person(data.getFnr(), data.getName());
        }

        // Update person record with AP data
        updatePersonWithAPData(person, data);

        // Calculate pension amounts
        calculatePensionAmounts(person);

        return new APRegistrationResult(true, person, null);
    }

    /**
     * Validates AP registration data.
     */
    public String validateAPData(APRegistrationData data) {
        if (data == null) {
            return "Registreringsdata mangler";
        }

        // Validate FNR
        if (data.getFnr() == null || data.getFnr().trim().isEmpty()) {
            return "FØDSELSNUMMER er påkrevd";
        }
        if (!validationService.validateFNR(data.getFnr())) {
            return "Ugyldig FØDSELSNUMMER";
        }

        // Validate name
        if (data.getName() == null || data.getName().trim().isEmpty()) {
            return "NAVN er påkrevd";
        }

        // Validate trygdetid (0-60 years)
        if (data.getTrygdetid() < 0 || data.getTrygdetid() > 60) {
            return "TRYGDETID må være mellom 0 og 60 år";
        }

        // Validate grunnpensjon (non-negative)
        if (data.getGrunnpensjon() < 0) {
            return "GRUNNPENSJON kan ikke være negativ";
        }

        // Validate saertekst length (max 20 chars)
        if (data.getSaertekst() != null && data.getSaertekst().length() > 20) {
            return "SÆRTEKST kan maksimalt være 20 tegn";
        }

        return null; // Valid
    }

    /**
     * Updates person record with AP data.
     */
    private void updatePersonWithAPData(Person person, APRegistrationData data) {
        // Update basic info
        if (data.getName() != null && !data.getName().trim().isEmpty()) {
            person.setName(data.getName());
        }

        // Update Age Pension segment
        AgePension ap = person.getAgePension();
        ap.setGp(data.getGrunnpensjon());
        
        // Store trygdetid in a field (using tp temporarily)
        // In full implementation, we need a dedicated trygdetid field
        ap.setTp(data.getTrygdetid() * 1000); // Scaled for storage

        // Update Pension Status
        PensionStatus ps = person.getPensionStatus();
        ps.setPensionType1(PensionType.AP.getCode());
        ps.setStatusCode(PensionStatusCode.ACTIVE);
        
        // Set effective date (virkningsdato) to today
        ps.setEffectiveDate(java.time.LocalDate.now());
    }

    /**
     * Calculates pension amounts based on trygdetid and other factors.
     * This is a simplified version. Full implementation would include:
     * - G-run calculation (grunnbeløp)
     * - Trygdetid calculation (R0019909)
     * - Special supplement calculations
     * - SPT/OPT/PÅ calculations
     */
    public void calculatePensionAmounts(Person person) {
        AgePension ap = person.getAgePension();
        PensionStatus ps = person.getPensionStatus();

        // Get current G-run (grunnbeløp) - simplified
        // In 2024, G = 116,011 NOK
        int gRun = 116011;

        // Calculate trygdetid from stored value
        int trygdetid = ap.getTp() / 1000;

        // Calculate GP (Grunnpensjon)
        // Full rate = 1.0 * G, reduced by trygdetid/40
        double trygdetidFactor = Math.min(trygdetid, 40) / 40.0;
        int gp = (int) (gRun * trygdetidFactor);
        ap.setGp(gp);

        // Calculate TP (Tilleggspensjon) - simplified
        // In reality, this is based on income history
        // Using 0.45 * G as a simplified rate
        int tp = (int) (gRun * 0.45 * trygdetidFactor);
        ap.setTp(tp);

        // Calculate total pension
        int totalPension = gp + tp;
        ps.setGpBrutto(gp);
        ps.setTpBrutto(tp);
    }

    /**
     * Calculates age from FNR and checks eligibility (67+ years).
     */
    public boolean checkAgeEligibility(String fnr) {
        int age = validationService.calculateAge(fnr);
        return age >= 67;
    }

    /**
     * Gets eligibility status message.
     */
    public String getEligibilityStatus(String fnr) {
        int age = validationService.calculateAge(fnr);
        
        if (age < 0) {
            return "Kan ikke beregne alder fra FNR";
        }
        
        if (age >= 67) {
            return "Alder: " + age + " år - BERETTIGET";
        } else {
            return "Alder: " + age + " år - IKKE BERETTIGET (må være 67+)";
        }
    }

    /**
     * Creates AP registration data from user input.
     */
    public APRegistrationData createRegistrationData(String fnr, String name, 
                                                      String trygdetidStr, String grunnpensjonStr, 
                                                      String saertekst) {
        APRegistrationData data = new APRegistrationData();
        data.setFnr(fnr);
        data.setName(name);
        
        try {
            data.setTrygdetid(trygdetidStr != null ? Integer.parseInt(trygdetidStr.trim()) : 0);
        } catch (NumberFormatException e) {
            data.setTrygdetid(0);
        }
        
        try {
            data.setGrunnpensjon(grunnpensjonStr != null ? Integer.parseInt(grunnpensjonStr.trim()) : 0);
        } catch (NumberFormatException e) {
            data.setGrunnpensjon(0);
        }
        
        data.setSaertekst(saertekst);
        
        return data;
    }
}

package com.nav.dsf.domain.service;

import com.nav.dsf.domain.model.DisabilityPension;
import com.nav.dsf.domain.model.PensionStatus;
import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.enums.PensionType;
import com.nav.dsf.domain.enums.PensionStatusCode;

/**
 * Service for Disability Pension (Uførepensjon) calculations and processing.
 * Implements business logic from R0010601 and related programs.
 * 
 * References original PL/I programs:
 * - R0010601: UP main control
 * - R0010602-R0010610: UP sub-programs
 * - R0019905: Age calculation
 * - Chapter 8 requirements validation
 */
public class DisabilityPensionService {

    private final ValidationService validationService;

    public DisabilityPensionService(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * UP Registration result.
     */
    public static class UPRegistrationResult {
        private final boolean success;
        private final Person person;
        private final String errorMessage;

        public UPRegistrationResult(boolean success, Person person, String errorMessage) {
            this.success = success;
            this.person = person;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() { return success; }
        public Person getPerson() { return person; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * UP Registration data.
     */
    public static class UPRegistrationData {
        private String fnr;
        private String name;
        private int uforhetsgrad; // Disability percentage (0-100)
        private String sykdomskode; // Disease code
        private String yrkeskode; // Occupation code
        private int grunnpensjon; // Basic pension amount
        private String saertekst; // Special text

        public UPRegistrationData() {}

        public UPRegistrationData(String fnr, String name, int uforhetsgrad, 
                                   String sykdomskode, String yrkeskode, 
                                   int grunnpensjon, String saertekst) {
            this.fnr = fnr;
            this.name = name;
            this.uforhetsgrad = uforhetsgrad;
            this.sykdomskode = sykdomskode;
            this.yrkeskode = yrkeskode;
            this.grunnpensjon = grunnpensjon;
            this.saertekst = saertekst;
        }

        // Getters and Setters
        public String getFnr() { return fnr; }
        public void setFnr(String fnr) { this.fnr = fnr; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getUforhetsgrad() { return uforhetsgrad; }
        public void setUforhetsgrad(int uforhetsgrad) { this.uforhetsgrad = uforhetsgrad; }
        public String getSykdomskode() { return sykdomskode; }
        public void setSykdomskode(String sykdomskode) { this.sykdomskode = sykdomskode; }
        public String getYrkeskode() { return yrkeskode; }
        public void setYrkeskode(String yrkeskode) { this.yrkeskode = yrkeskode; }
        public int getGrunnpensjon() { return grunnpensjon; }
        public void setGrunnpensjon(int grunnpensjon) { this.grunnpensjon = grunnpensjon; }
        public String getSaertekst() { return saertekst; }
        public void setSaertekst(String saertekst) { this.saertekst = saertekst; }
    }

    /**
     * Registers Disability Pension for a person.
     */
    public UPRegistrationResult registerDisabilityPension(UPRegistrationData data, Person person) {
        // Validate input data
        String validationError = validateUPData(data);
        if (validationError != null) {
            return new UPRegistrationResult(false, null, validationError);
        }

        // Create new person if not provided
        if (person == null) {
            person = new Person(data.getFnr(), data.getName());
        }

        // Update person record with UP data
        updatePersonWithUPData(person, data);

        // Calculate pension amounts
        calculatePensionAmounts(person);

        return new UPRegistrationResult(true, person, null);
    }

    /**
     * Validates UP registration data.
     */
    public String validateUPData(UPRegistrationData data) {
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

        // Validate disability grade (20-100%)
        if (data.getUforhetsgrad() < 20 || data.getUforhetsgrad() > 100) {
            return "UFØRHETSGRAD må være mellom 20 og 100 prosent";
        }

        // Validate sykdomskode (max 10 chars)
        if (data.getSykdomskode() != null && data.getSykdomskode().length() > 10) {
            return "SYKDOMSKODE kan maksimalt være 10 tegn";
        }

        // Validate yrkeskode (4 digits)
        if (data.getYrkeskode() != null && !data.getYrkeskode().matches("\\d{4}")) {
            return "YRKESKODE må være 4 siffer";
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
     * Updates person record with UP data.
     */
    private void updatePersonWithUPData(Person person, UPRegistrationData data) {
        // Update basic info
        if (data.getName() != null && !data.getName().trim().isEmpty()) {
            person.setName(data.getName());
        }

        // Update Disability Pension segment
        DisabilityPension up = person.getDisabilityPension();
        up.setGp(data.getGrunnpensjon());
        up.setUforhetsgrad(data.getUforhetsgrad());
        
        if (data.getSykdomskode() != null) {
            up.setDiagnosekode(data.getSykdomskode());
        }
        if (data.getYrkeskode() != null) {
            up.setYrkeskode(data.getYrkeskode());
        }

        // Update Pension Status
        PensionStatus ps = person.getPensionStatus();
        ps.setPensionType1(PensionType.UP.getCode());
        ps.setStatusCode(PensionStatusCode.ACTIVE);
        ps.setEffectiveDate(java.time.LocalDate.now());
    }

    /**
     * Calculates pension amounts based on disability grade and other factors.
     */
    public void calculatePensionAmounts(Person person) {
        DisabilityPension up = person.getDisabilityPension();
        PensionStatus ps = person.getPensionStatus();

        // Get current G-run (grunnbeløp) - simplified
        int gRun = 116011;

        // Calculate disability factor (uførhetsgrad / 100)
        double uforhetFactor = up.getUforhetsgrad() / 100.0;

        // Calculate GP (Grunnpensjon)
        int gp = (int) (gRun * uforhetFactor);
        up.setGp(gp);

        // Calculate TP (Tilleggspensjon) - simplified
        int tp = (int) (gRun * 0.45 * uforhetFactor);
        up.setTp(tp);

        // Update status
        ps.setGpBrutto(gp);
        ps.setTpBrutto(tp);
    }

    /**
     * Checks if disability grade meets minimum requirement (20%).
     */
    public boolean checkDisabilityEligibility(int uforhetsgrad) {
        return uforhetsgrad >= 20;
    }

    /**
     * Gets eligibility status message.
     */
    public String getEligibilityStatus(int uforhetsgrad) {
        if (uforhetsgrad < 20) {
            return "UFØRHETSGRAD: " + uforhetsgrad + "% - IKKE BERETTIGET (må være minst 20%)";
        } else if (uforhetsgrad >= 100) {
            return "UFØRHETSGRAD: " + uforhetsgrad + "% - HELT UFØR";
        } else {
            return "UFØRHETSGRAD: " + uforhetsgrad + "% - DELVIS UFØR";
        }
    }

    /**
     * Creates UP registration data from user input.
     */
    public UPRegistrationData createRegistrationData(String fnr, String name,
                                                      String uforhetsgradStr, String sykdomskode,
                                                      String yrkeskode, String grunnpensjonStr,
                                                      String saertekst) {
        UPRegistrationData data = new UPRegistrationData();
        data.setFnr(fnr);
        data.setName(name);
        
        try {
            data.setUforhetsgrad(uforhetsgradStr != null ? Integer.parseInt(uforhetsgradStr.trim()) : 0);
        } catch (NumberFormatException e) {
            data.setUforhetsgrad(0);
        }
        
        data.setSykdomskode(sykdomskode);
        data.setYrkeskode(yrkeskode);
        
        try {
            data.setGrunnpensjon(grunnpensjonStr != null ? Integer.parseInt(grunnpensjonStr.trim()) : 0);
        } catch (NumberFormatException e) {
            data.setGrunnpensjon(0);
        }
        
        data.setSaertekst(saertekst);
        
        return data;
    }
}

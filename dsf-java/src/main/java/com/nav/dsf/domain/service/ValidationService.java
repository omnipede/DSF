package com.nav.dsf.domain.service;

import com.nav.dsf.domain.model.Person;
import com.nav.dsf.common.util.DateUtil;
import com.nav.dsf.common.util.FNRValidator;

/**
 * Validation service for DSF data.
 * Provides validation routines similar to the original PL/I programs.
 * 
 * References:
 * - R0019904: FNR validation
 * - R0019905: Age calculation
 * - R0019982: Date conversion
 */
public class ValidationService {

    /**
     * Validates a Norwegian FNR (fødselsnummer).
     * 
     * @param fnr The FNR to validate
     * @return true if valid, false otherwise
     */
    public boolean validateFNR(String fnr) {
        return FNRValidator.isValid(fnr);
    }

    /**
     * Validates a date in DSF format (yyyyMMdd).
     * 
     * @param dateStr The date string to validate
     * @return true if valid, false otherwise
     */
    public boolean validateDate(String dateStr) {
        return DateUtil.parseCompact(dateStr) != null;
    }

    /**
     * Validates a pension type code.
     * 
     * @param pensionType The pension type code
     * @return true if valid, false otherwise
     */
    public boolean validatePensionType(String pensionType) {
        if (pensionType == null || pensionType.trim().isEmpty()) {
            return false;
        }
        
        String[] validTypes = {
            "AP", "UP", "EP", "FB", "BP", "FT", "YK", "AF",
            "TG", "E1", "O1", "O2", "MK", "UF"
        };
        
        for (String valid : validTypes) {
            if (valid.equalsIgnoreCase(pensionType.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Validates a function code.
     * 
     * @param functionCode The function code
     * @return true if valid, false otherwise
     */
    public boolean validateFunctionCode(String functionCode) {
        if (functionCode == null || functionCode.length() != 1) {
            return false;
        }
        
        String validCodes = "AFRIVX";
        return validCodes.contains(functionCode.toUpperCase());
    }

    /**
     * Validates a registration code.
     * 
     * @param regCode The registration code
     * @return true if valid, false otherwise
     */
    public boolean validateRegCode(String regCode) {
        if (regCode == null || regCode.trim().isEmpty()) {
            return false;
        }
        
        String[] validCodes = {
            "AP", "A1", "UP", "U2", "U3", "US",
            "EP", "E3", "EF", "E4",
            "FB", "F5", "BP", "B6",
            "FT", "F7", "FO",
            "TG", "E1", "EN", "YK",
            "O1", "O2", "MK", "AF", "UF",
            "XX"
        };
        
        for (String valid : validCodes) {
            if (valid.equalsIgnoreCase(regCode.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Calculates age from FNR.
     * 
     * @param fnr The FNR
     * @return Age in years, or -1 if invalid
     */
    public int calculateAge(String fnr) {
        if (!validateFNR(fnr)) {
            return -1;
        }
        return FNRValidator.getAge(fnr);
    }

    /**
     * Extracts gender from FNR.
     * 
     * @param fnr The FNR
     * @return "M" for male, "F" for female, null if invalid
     */
    public String getGender(String fnr) {
        if (!validateFNR(fnr)) {
            return null;
        }
        return FNRValidator.getGender(fnr);
    }

    /**
     * Validates a person record for completeness.
     * 
     * @param person The person to validate
     * @return Error message if invalid, null if valid
     */
    public String validatePerson(Person person) {
        if (person == null) {
            return "Person cannot be null";
        }
        
        if (person.getFnr() == null || person.getFnr().trim().isEmpty()) {
            return "FNR is required";
        }
        
        if (!validateFNR(person.getFnr())) {
            return "Invalid FNR format";
        }
        
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            return "Name is required";
        }
        
        return null; // Valid
    }

    /**
     * Validates a numeric field.
     * 
     * @param value The value to validate
     * @param fieldName The field name (for error message)
     * @param min Minimum value
     * @param max Maximum value
     * @return Error message if invalid, null if valid
     */
    public String validateNumericField(Integer value, String fieldName, int min, int max) {
        if (value == null) {
            return fieldName + " is required";
        }
        
        if (value < min || value > max) {
            return fieldName + " must be between " + min + " and " + max;
        }
        
        return null;
    }

    /**
     * Validates a string field.
     * 
     * @param value The value to validate
     * @param fieldName The field name (for error message)
     * @param maxLength Maximum length
     * @param required Whether the field is required
     * @return Error message if invalid, null if valid
     */
    public String validateStringField(String value, String fieldName, int maxLength, boolean required) {
        if (value == null || value.trim().isEmpty()) {
            if (required) {
                return fieldName + " is required";
            }
            return null;
        }
        
        if (value.length() > maxLength) {
            return fieldName + " exceeds maximum length of " + maxLength;
        }
        
        return null;
    }
}

package com.nav.dsf.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Norwegian national ID (FNR - Fødselsnummer) validator.
 * Implements the checksum algorithm used in the original PL/I program R0019904.
 * 
 * FNR structure: DDMMYYXXXCC
 * - DDMMYY: Date of birth
 * - XXX: Individual number
 * - CC: Check digits (calculated using modulo 11)
 */
public class FNRValidator {

    private static final int[] WEIGHTS_FIRST = {3, 7, 6, 1, 8, 9, 4, 5, 2};
    private static final int[] WEIGHTS_SECOND = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    /**
     * Validates a Norwegian FNR.
     * 
     * @param fnr The 11-digit FNR to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String fnr) {
        if (fnr == null || fnr.length() != 11) {
            return false;
        }

        // Check all digits
        if (!fnr.matches("\\d{11}")) {
            return false;
        }

        // Calculate first check digit
        int sum1 = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(fnr.charAt(i));
            sum1 += digit * WEIGHTS_FIRST[i];
        }
        int remainder1 = sum1 % 11;
        int checkDigit1 = (remainder1 == 0) ? 0 : (11 - remainder1);

        // Calculate second check digit
        int sum2 = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(fnr.charAt(i));
            sum2 += digit * WEIGHTS_SECOND[i];
        }
        if (checkDigit1 < 10) {
            sum2 += checkDigit1 * WEIGHTS_SECOND[9];
        }
        int remainder2 = sum2 % 11;
        int checkDigit2 = (remainder2 == 0) ? 0 : (11 - remainder2);

        // Validate check digits
        int actualCheck1 = Character.getNumericValue(fnr.charAt(9));
        int actualCheck2 = Character.getNumericValue(fnr.charAt(10));

        return (checkDigit1 == actualCheck1 && checkDigit2 == actualCheck2);
    }

    /**
     * Extracts birth date from FNR.
     * Note: Century interpretation is ambiguous for FNRs without context.
     * 
     * @param fnr The FNR
     * @return LocalDate if parseable, null otherwise
     */
    public static LocalDate getBirthDate(String fnr) {
        if (!isValid(fnr)) {
            return null;
        }

        try {
            int day = Integer.parseInt(fnr.substring(0, 2));
            int month = Integer.parseInt(fnr.substring(2, 4));
            int year = Integer.parseInt(fnr.substring(4, 6));

            // Determine century based on individual number
            int individualNumber = Integer.parseInt(fnr.substring(6, 9));
            int fullYear;

            if (individualNumber >= 0 && individualNumber <= 499) {
                // 1900-1999 or 2000-2039
                if (year >= 0 && year <= 39) {
                    fullYear = 2000 + year;
                } else {
                    fullYear = 1900 + year;
                }
            } else if (individualNumber >= 500 && individualNumber <= 749) {
                // 1800-1899
                fullYear = 1800 + year;
            } else if (individualNumber >= 900 && individualNumber <= 999) {
                // 1940-1999
                fullYear = 1900 + year;
            } else {
                // 1940-1999 (default)
                fullYear = 1900 + year;
            }

            return LocalDate.of(fullYear, month, day);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extracts gender from FNR.
     * Even individual number = Female, Odd = Male
     * 
     * @param fnr The FNR
     * @return "M" for male, "F" for female, null if invalid
     */
    public static String getGender(String fnr) {
        if (!isValid(fnr)) {
            return null;
        }

        int individualNumber = Integer.parseInt(fnr.substring(6, 9));
        return (individualNumber % 2 == 0) ? "F" : "M";
    }

    /**
     * Calculates age from FNR.
     * 
     * @param fnr The FNR
     * @return Age in years, or -1 if invalid
     */
    public static int getAge(String fnr) {
        LocalDate birthDate = getBirthDate(fnr);
        if (birthDate == null) {
            return -1;
        }

        LocalDate today = LocalDate.now();
        int age = today.getYear() - birthDate.getYear();

        if (today.getMonthValue() < birthDate.getMonthValue() ||
            (today.getMonthValue() == birthDate.getMonthValue() &&
             today.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        return age;
    }

    /**
     * Formats FNR for display (XXXXXX XXXXX).
     * 
     * @param fnr The FNR
     * @return Formatted string
     */
    public static String format(String fnr) {
        if (fnr == null || fnr.length() != 11) {
            return fnr;
        }
        return fnr.substring(0, 6) + " " + fnr.substring(6);
    }
}

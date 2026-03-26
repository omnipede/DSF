package com.nav.dsf.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Date utilities for DSF system.
 * Handles Norwegian date formats and conversions.
 * 
 * Original PL/I date handling from R0019982.
 */
public class DateUtil {

    private static final DateTimeFormatter NORWEGIAN_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter COMPACT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Parses a date from the DSF compact format (yyyyMMdd).
     * 
     * @param dateStr Date string in yyyyMMdd format
     * @return LocalDate or null if invalid
     */
    public static LocalDate parseCompact(String dateStr) {
        if (dateStr == null || dateStr.length() != 8) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, COMPACT_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Parses a date from the Norwegian format (yyyy.MM.dd).
     * 
     * @param dateStr Date string in yyyy.MM.dd format
     * @return LocalDate or null if invalid
     */
    public static LocalDate parseNorwegian(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, NORWEGIAN_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Formats a date to DSF compact format (yyyyMMdd).
     * 
     * @param date The date to format
     * @return Formatted string or empty string if null
     */
    public static String formatCompact(LocalDate date) {
        if (date == null) {
            return "        ";
        }
        return date.format(COMPACT_FORMAT);
    }

    /**
     * Formats a date to Norwegian format (yyyy.MM.dd).
     * 
     * @param date The date to format
     * @return Formatted string or empty string if null
     */
    public static String formatNorwegian(LocalDate date) {
        if (date == null) {
            return "          ";
        }
        return date.format(NORWEGIAN_FORMAT);
    }

    /**
     * Formats a date to short Norwegian format (dd.MM.yyyy).
     * 
     * @param date The date to format
     * @return Formatted string or empty string if null
     */
    public static String formatShort(LocalDate date) {
        if (date == null) {
            return "          ";
        }
        return date.format(SHORT_FORMAT);
    }

    /**
     * Calculates age in years from birth date.
     * 
     * @param birthDate The birth date
     * @return Age in years
     */
    public static int calculateAge(LocalDate birthDate) {
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
     * Calculates age in years from FNR (Norwegian national ID).
     * 
     * @param fnr The 11-digit FNR
     * @return Age in years, or -1 if invalid
     */
    public static int calculateAgeFromFNR(String fnr) {
        LocalDate birthDate = FNRValidator.getBirthDate(fnr);
        return calculateAge(birthDate);
    }

    /**
     * Gets current date in DSF compact format.
     * 
     * @return Current date as yyyyMMdd string
     */
    public static String getCurrentDateCompact() {
        return LocalDate.now().format(COMPACT_FORMAT);
    }

    /**
     * Gets current date in Norwegian format.
     * 
     * @return Current date as yyyy.MM.dd string
     */
    public static String getCurrentDateNorwegian() {
        return LocalDate.now().format(NORWEGIAN_FORMAT);
    }

    /**
     * Validates if a date string is valid.
     * 
     * @param dateStr Date string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDate(String dateStr) {
        return parseCompact(dateStr) != null || parseNorwegian(dateStr) != null;
    }

    /**
     * Adds years to a date.
     * 
     * @param date The base date
     * @param years Years to add (can be negative)
     * @return New date
     */
    public static LocalDate addYears(LocalDate date, int years) {
        if (date == null) {
            return null;
        }
        return date.plusYears(years);
    }

    /**
     * Adds months to a date.
     * 
     * @param date The base date
     * @param months Months to add (can be negative)
     * @return New date
     */
    public static LocalDate addMonths(LocalDate date, int months) {
        if (date == null) {
            return null;
        }
        return date.plusMonths(months);
    }

    /**
     * Calculates the number of complete years between two dates.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return Number of complete years
     */
    public static int yearsBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        return java.time.Period.between(startDate, endDate).getYears();
    }

    /**
     * Calculates the number of complete months between two dates.
     *
     * @param startDate Start date
     * @param endDate End date
     * @return Number of complete months
     */
    public static long monthsBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        return java.time.Period.between(startDate, endDate).toTotalMonths();
    }
}

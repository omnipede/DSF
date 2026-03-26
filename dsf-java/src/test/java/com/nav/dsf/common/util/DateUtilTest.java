package com.nav.dsf.common.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Unit tests for DateUtil.
 */
class DateUtilTest {

    @Test
    void testParseCompact() {
        LocalDate date = DateUtil.parseCompact("20230326");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(3, date.getMonthValue());
        assertEquals(26, date.getDayOfMonth());
    }

    @Test
    void testParseCompact_Invalid() {
        assertNull(DateUtil.parseCompact(null));
        assertNull(DateUtil.parseCompact(""));
        assertNull(DateUtil.parseCompact("2023032"));
        assertNull(DateUtil.parseCompact("202303261"));
        assertNull(DateUtil.parseCompact("abcdefgh"));
    }

    @Test
    void testParseNorwegian() {
        LocalDate date = DateUtil.parseNorwegian("2023.03.26");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(3, date.getMonthValue());
        assertEquals(26, date.getDayOfMonth());
    }

    @Test
    void testFormatCompact() {
        LocalDate date = LocalDate.of(2023, 3, 26);
        assertEquals("20230326", DateUtil.formatCompact(date));
    }

    @Test
    void testFormatCompact_Null() {
        assertEquals("        ", DateUtil.formatCompact(null));
    }

    @Test
    void testFormatNorwegian() {
        LocalDate date = LocalDate.of(2023, 3, 26);
        assertEquals("2023.03.26", DateUtil.formatNorwegian(date));
    }

    @Test
    void testCalculateAge() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        int age = DateUtil.calculateAge(birthDate);
        assertTrue(age > 0 && age < 100);
    }

    @Test
    void testCalculateAge_Null() {
        assertEquals(-1, DateUtil.calculateAge(null));
    }

    @Test
    void testGetCurrentDateCompact() {
        String currentDate = DateUtil.getCurrentDateCompact();
        assertNotNull(currentDate);
        assertEquals(8, currentDate.length());
        assertTrue(currentDate.matches("\\d{8}"));
    }

    @Test
    void testAddYears() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalDate result = DateUtil.addYears(date, 5);
        assertEquals(2025, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    void testAddMonths() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalDate result = DateUtil.addMonths(date, 3);
        assertEquals(2020, result.getYear());
        assertEquals(4, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    void testYearsBetween() {
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 1);
        assertEquals(3, DateUtil.yearsBetween(start, end));
    }

    @Test
    void testMonthsBetween() {
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 6, 1);
        assertEquals(5, DateUtil.monthsBetween(start, end));
    }
}

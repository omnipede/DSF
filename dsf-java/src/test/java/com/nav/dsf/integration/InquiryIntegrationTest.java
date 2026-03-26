package com.nav.dsf.integration;

import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.repository.MemoryDatabase;
import com.nav.dsf.domain.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Inquiry (Forespørsel).
 * Tests person lookup and display functionality.
 */
class InquiryIntegrationTest {

    private PersonService personService;
    private MemoryDatabase database;

    @BeforeEach
    void setUp() {
        database = new MemoryDatabase();
        personService = new PersonService(database);
    }

    @Test
    @DisplayName("FNR 조회 - 존재하는 사람")
    void inquiry_PersonExists() {
        // Given - Demo data is loaded
        String fnr = "01010100131";

        // When - Lookup person
        var personOpt = personService.findByFnr(fnr);

        // Then - Person found
        assertTrue(personOpt.isPresent());
        Person person = personOpt.get();
        assertEquals(fnr, person.getFnr());
        assertNotNull(person.getName());
        assertNotNull(person.getAgePension());
        assertNotNull(person.getPensionStatus());
    }

    @Test
    @DisplayName("FNR 조회 - 존재하지 않는 사람")
    void inquiry_PersonNotExists() {
        // Given - Non-existent FNR
        String fnr = "99999999999";

        // When - Lookup person
        var personOpt = personService.findByFnr(fnr);

        // Then - Person not found
        assertFalse(personOpt.isPresent());
    }

    @Test
    @DisplayName("FNR 조회 - null FNR")
    void inquiry_NullFNR() {
        // When - Lookup with null
        var personOpt = personService.findByFnr(null);

        // Then - Empty result
        assertFalse(personOpt.isPresent());
    }

    @Test
    @DisplayName("FNR 조회 - 빈 문자열 FNR")
    void inquiry_EmptyFNR() {
        // When - Lookup with empty string
        var personOpt = personService.findByFnr("");

        // Then - Empty result
        assertFalse(personOpt.isPresent());
    }

    @Test
    @DisplayName("모든 사람 조회")
    void inquiry_AllPersons() {
        // When - Get all persons
        var allPersons = personService.getAllPersons();

        // Then - At least demo data
        assertNotNull(allPersons);
        assertTrue(allPersons.size() >= 4);
    }

    @Test
    @DisplayName("사람 수 카운트")
    void inquiry_Count() {
        // When - Count persons
        int count = personService.count();

        // Then - Demo data count
        assertEquals(4, count);
    }

    @Test
    @DisplayName("사람 존재 확인")
    void inquiry_Exists() {
        // When & Then
        assertTrue(personService.personExists("01010100131"));
        assertFalse(personService.personExists("99999999999"));
    }
}

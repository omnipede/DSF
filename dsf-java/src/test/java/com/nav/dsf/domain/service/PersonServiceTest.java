package com.nav.dsf.domain.service;

import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.repository.MemoryDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PersonService.
 */
class PersonServiceTest {

    private PersonService personService;
    private MemoryDatabase database;

    @BeforeEach
    void setUp() {
        database = new MemoryDatabase();
        personService = new PersonService(database);
    }

    @Test
    @DisplayName("FNR 로 사람 찾기 - 존재함")
    void findPerson_Exists() {
        // Given - Demo data already loaded
        // When
        var personOpt = personService.findByFnr("01010100131");

        // Then
        assertTrue(personOpt.isPresent());
        assertEquals("01010100131", personOpt.get().getFnr());
    }

    @Test
    @DisplayName("FNR 로 사람 찾기 - 존재하지 않음")
    void findPerson_NotExists() {
        // When
        var personOpt = personService.findByFnr("99999999999");

        // Then
        assertFalse(personOpt.isPresent());
    }

    @Test
    @DisplayName("사람 찾기 또는 null - 존재함")
    void findPersonOrNull_Exists() {
        // When
        Person person = personService.findPersonOrNull("01010100131");

        // Then
        assertNotNull(person);
        assertEquals("01010100131", person.getFnr());
    }

    @Test
    @DisplayName("사람 찾기 또는 null - 존재하지 않음")
    void findPersonOrNull_NotExists() {
        // When
        Person person = personService.findPersonOrNull("99999999999");

        // Then
        assertNull(person);
    }

    @Test
    @DisplayName("새 사람 저장")
    void savePerson_NewPerson() {
        // Given
        Person person = new Person("13050300182", "Per Hansen");

        // When
        Person saved = personService.savePerson(person);

        // Then
        assertNotNull(saved);
        assertTrue(personService.personExists("13050300182"));
        // Note: 13050300182 might already exist in demo data
        assertTrue(personService.count() >= 4); // At least demo data
    }

    @Test
    @DisplayName("기존 사람 수정")
    void savePerson_UpdateExisting() {
        // Given
        Person person = personService.findPersonOrNull("01010100131");
        assertNotNull(person);
        String originalName = person.getName();

        // When
        person.setName("Updated Name");
        personService.savePerson(person);

        // Then
        Person updated = personService.findPersonOrNull("01010100131");
        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    @DisplayName("사람 삭제 - 성공")
    void deletePerson_Success() {
        // Given
        Person person = new Person("13050300182", "Per Hansen");
        personService.savePerson(person);
        assertTrue(personService.personExists("13050300182"));

        // When
        boolean deleted = personService.deletePerson("13050300182");

        // Then
        assertTrue(deleted);
        assertFalse(personService.personExists("13050300182"));
    }

    @Test
    @DisplayName("사람 삭제 - 존재하지 않음")
    void deletePerson_NotExists() {
        // When
        boolean deleted = personService.deletePerson("99999999999");

        // Then
        assertFalse(deleted);
    }

    @Test
    @DisplayName("사람 존재 확인 - 참")
    void personExists_True() {
        // When & Then
        assertTrue(personService.personExists("01010100131"));
    }

    @Test
    @DisplayName("사람 존재 확인 - 거짓")
    void personExists_False() {
        // When & Then
        assertFalse(personService.personExists("99999999999"));
    }

    @Test
    @DisplayName("모든 사람 가져오기")
    void getAllPersons() {
        // When
        var allPersons = personService.getAllPersons();

        // Then
        assertNotNull(allPersons);
        assertTrue(allPersons.size() >= 4); // At least demo data
    }

    @Test
    @DisplayName("사람 수 카운트")
    void count() {
        // When
        int count = personService.count();

        // Then
        assertEquals(4, count); // Demo data
    }

    @Test
    @DisplayName("사람 저장 - null 예외")
    void savePerson_NullPerson() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            personService.savePerson(null);
        });
    }
}

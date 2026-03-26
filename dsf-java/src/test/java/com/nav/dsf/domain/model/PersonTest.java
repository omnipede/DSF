package com.nav.dsf.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Person.
 */
class PersonTest {

    @Test
    @DisplayName("Person 생성자 - FNR 과 이름")
    void constructor_FnrAndName() {
        // When
        Person person = new Person("01010100131", "Ola Nordmann");

        // Then
        assertEquals("01010100131", person.getFnr());
        assertEquals("Ola Nordmann", person.getName());
    }

    @Test
    @DisplayName("Person 생성자 - 초기값 확인")
    void constructor_DefaultValues() {
        // When
        Person person = new Person("01010100131", "Ola");

        // Then
        assertEquals(0, person.getTknr());
        assertEquals(" ", person.getLanguage()); // Default is space, not "N"
        assertNotNull(person.getAgePension());
        assertNotNull(person.getDisabilityPension());
        assertNotNull(person.getPensionStatus());
    }

    @Test
    @DisplayName("AgePension 가져오기 - 초기값")
    void getAgePension_Default() {
        // Given
        Person person = new Person("01010100131", "Ola");

        // When
        AgePension ap = person.getAgePension();

        // Then
        assertNotNull(ap);
        assertEquals(0, ap.getGp());
        assertEquals(0, ap.getTp());
    }

    @Test
    @DisplayName("DisabilityPension 가져오기 - 초기값")
    void getDisabilityPension_Default() {
        // Given
        Person person = new Person("01010100131", "Ola");

        // When
        DisabilityPension dp = person.getDisabilityPension();

        // Then
        assertNotNull(dp);
        assertEquals(0, dp.getUforhetsgrad());
        assertEquals(0, dp.getGp());
    }

    @Test
    @DisplayName("PensionStatus 가져오기 - 초기값")
    void getPensionStatus_Default() {
        // Given
        Person person = new Person("01010100131", "Ola");

        // When
        PensionStatus ps = person.getPensionStatus();

        // Then
        assertNotNull(ps);
        assertEquals(' ', ps.getStatusCode().getCode());
        assertEquals(" ", ps.getPensionType1());
    }

    @Test
    @DisplayName("이름 설정 및 가져오기")
    void setNameAndGet() {
        // Given
        Person person = new Person("01010100131", "Ola");

        // When
        person.setName("Kari Nordmann");

        // Then
        assertEquals("Kari Nordmann", person.getName());
    }

    @Test
    @DisplayName("TKNR 설정 및 가져오기")
    void setTknrAndGet() {
        // Given
        Person person = new Person("01010100131", "Ola");

        // When
        person.setTknr(1234);

        // Then
        assertEquals(1234, person.getTknr());
    }

    @Test
    @DisplayName("언어 설정 및 가져오기")
    void setLanguageAndGet() {
        // Given
        Person person = new Person("01010100131", "Ola");

        // When
        person.setLanguage("E");

        // Then
        assertEquals("E", person.getLanguage());
    }

    @Test
    @DisplayName("toString - null 아님")
    void toString_NotNull() {
        // Given
        Person person = new Person("01010100131", "Ola");

        // When
        String str = person.toString();

        // Then
        assertNotNull(str);
        assertTrue(str.contains("01010100131"));
    }
}

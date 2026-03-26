package com.nav.dsf.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AgePension.
 */
class AgePensionTest {

    @Test
    @DisplayName("AgePension 생성자 - 초기값")
    void constructor_DefaultValues() {
        // When
        AgePension ap = new AgePension();

        // Then
        assertEquals(0, ap.getGp());
        assertEquals(0, ap.getTp());
    }

    @Test
    @DisplayName("Grunnpensjon 설정 및 가져오기")
    void setGpAndGet() {
        // Given
        AgePension ap = new AgePension();

        // When
        ap.setGp(100000);

        // Then
        assertEquals(100000, ap.getGp());
    }

    @Test
    @DisplayName("Tilleggspensjon 설정 및 가져오기")
    void setTpAndGet() {
        // Given
        AgePension ap = new AgePension();

        // When
        ap.setTp(50000);

        // Then
        assertEquals(50000, ap.getTp());
    }

    @Test
    @DisplayName("toString - null 아님")
    void toString_NotNull() {
        // Given
        AgePension ap = new AgePension();
        ap.setGp(100000);

        // When
        String str = ap.toString();

        // Then
        assertNotNull(str);
        assertTrue(str.contains("GP=100000"));
    }
}

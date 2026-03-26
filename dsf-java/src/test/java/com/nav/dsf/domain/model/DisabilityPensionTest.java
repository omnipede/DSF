package com.nav.dsf.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DisabilityPension.
 */
class DisabilityPensionTest {

    @Test
    @DisplayName("DisabilityPension 생성자 - 초기값")
    void constructor_DefaultValues() {
        // When
        DisabilityPension dp = new DisabilityPension();

        // Then
        assertEquals(0, dp.getUforhetsgrad());
        assertEquals(0, dp.getGp());
        assertEquals(0, dp.getTp());
        assertEquals(" ", dp.getDiagnosekode());
        assertEquals("0000", dp.getYrkeskode());
    }

    @Test
    @DisplayName("UFØRHETSGRAD 설정 및 가져오기")
    void setUforhetsgradAndGet() {
        // Given
        DisabilityPension dp = new DisabilityPension();

        // When
        dp.setUforhetsgrad(100);

        // Then
        assertEquals(100, dp.getUforhetsgrad());
    }

    @Test
    @DisplayName("Grunnpensjon 설정 및 가져오기")
    void setGpAndGet() {
        // Given
        DisabilityPension dp = new DisabilityPension();

        // When
        dp.setGp(100000);

        // Then
        assertEquals(100000, dp.getGp());
    }

    @Test
    @DisplayName("Tilleggspensjon 설정 및 가져오기")
    void setTpAndGet() {
        // Given
        DisabilityPension dp = new DisabilityPension();

        // When
        dp.setTp(50000);

        // Then
        assertEquals(50000, dp.getTp());
    }

    @Test
    @DisplayName("DIAGNOSEKODE 설정 및 가져오기")
    void setDiagnosekodeAndGet() {
        // Given
        DisabilityPension dp = new DisabilityPension();

        // When
        dp.setDiagnosekode("DIABETES");

        // Then
        assertEquals("DIABETES", dp.getDiagnosekode());
    }

    @Test
    @DisplayName("YRKESKODE 설정 및 가져오기")
    void setYrkeskodeAndGet() {
        // Given
        DisabilityPension dp = new DisabilityPension();

        // When
        dp.setYrkeskode("1234");

        // Then
        assertEquals("1234", dp.getYrkeskode());
    }

    @Test
    @DisplayName("toString - null 아님")
    void toString_NotNull() {
        // Given
        DisabilityPension dp = new DisabilityPension();
        dp.setUforhetsgrad(100);

        // When
        String str = dp.toString();

        // Then
        assertNotNull(str);
        assertTrue(str.contains("UFØRHETSGRAD=100"));
    }
}

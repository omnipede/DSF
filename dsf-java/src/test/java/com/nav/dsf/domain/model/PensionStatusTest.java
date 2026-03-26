package com.nav.dsf.domain.model;

import com.nav.dsf.domain.enums.PensionStatusCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PensionStatus.
 */
class PensionStatusTest {

    @Test
    @DisplayName("PensionStatus 생성자 - 초기값")
    void constructor_DefaultValues() {
        // When
        PensionStatus ps = new PensionStatus();

        // Then
        assertEquals(PensionStatusCode.ACTIVE, ps.getStatusCode());
        assertEquals(" ", ps.getPensionType1());
        assertEquals(" ", ps.getPensionType2());
        assertEquals(" ", ps.getPensionType3());
        assertEquals(" ", ps.getCivilStatus());
    }

    @Test
    @DisplayName("PENSIONSTYPE1 설정 및 가져오기")
    void setPensionType1AndGet() {
        // Given
        PensionStatus ps = new PensionStatus();

        // When
        ps.setPensionType1("AP");

        // Then
        assertEquals("AP", ps.getPensionType1());
    }

    @Test
    @DisplayName("STATUS_KODE 설정 및 가져오기")
    void setStatusCodeAndGet() {
        // Given
        PensionStatus ps = new PensionStatus();

        // When
        ps.setStatusCode(PensionStatusCode.DECEASED);

        // Then
        assertEquals(PensionStatusCode.DECEASED, ps.getStatusCode());
    }

    @Test
    @DisplayName("GP_BRUTTO 설정 및 가져오기")
    void setGpBruttoAndGet() {
        // Given
        PensionStatus ps = new PensionStatus();

        // When
        ps.setGpBrutto(100000);

        // Then
        assertEquals(100000, ps.getGpBrutto());
    }

    @Test
    @DisplayName("TP_BRUTTO 설정 및 가져오기")
    void setTpBruttoAndGet() {
        // Given
        PensionStatus ps = new PensionStatus();

        // When
        ps.setTpBrutto(50000);

        // Then
        assertEquals(50000, ps.getTpBrutto());
    }

    @Test
    @DisplayName("toString - null 아님")
    void toString_NotNull() {
        // Given
        PensionStatus ps = new PensionStatus();
        ps.setPensionType1("AP");

        // When
        String str = ps.toString();

        // Then
        assertNotNull(str);
        assertTrue(str.contains("AP"));
    }
}

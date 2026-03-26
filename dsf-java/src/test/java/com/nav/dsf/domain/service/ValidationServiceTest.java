package com.nav.dsf.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationService.
 */
class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    @Test
    @DisplayName("FNR 검증 - 유효한 FNR")
    void validateFNR_Valid() {
        // When & Then
        assertTrue(validationService.validateFNR("01010100131"));
        assertTrue(validationService.validateFNR("13050300182"));
    }

    @Test
    @DisplayName("FNR 검증 - 무효한 FNR")
    void validateFNR_Invalid() {
        // When & Then
        assertFalse(validationService.validateFNR("12345"));
        assertFalse(validationService.validateFNR("12345678901"));
        assertFalse(validationService.validateFNR("11111111111"));
    }

    @Test
    @DisplayName("날짜 검증 - 유효한 날짜")
    void validateDate_Valid() {
        // When & Then
        assertTrue(validationService.validateDate("20230326"));
        assertTrue(validationService.validateDate("20231231"));
    }

    @Test
    @DisplayName("날짜 검증 - 무효한 날짜")
    void validateDate_Invalid() {
        // When & Then
        assertFalse(validationService.validateDate(null));
        assertFalse(validationService.validateDate(""));
        assertFalse(validationService.validateDate("2023032"));
        assertFalse(validationService.validateDate("abcdefgh"));
    }

    @Test
    @DisplayName("연금타입 검증 - AP")
    void validatePensionType_AP() {
        // When & Then
        assertTrue(validationService.validatePensionType("AP"));
        assertTrue(validationService.validatePensionType("ap"));
    }

    @Test
    @DisplayName("연금타입 검증 - UP")
    void validatePensionType_UP() {
        // When & Then
        assertTrue(validationService.validatePensionType("UP"));
        assertTrue(validationService.validatePensionType("up"));
    }

    @Test
    @DisplayName("연금타입 검증 - 무효한 타입")
    void validatePensionType_Invalid() {
        // When & Then
        assertFalse(validationService.validatePensionType(null));
        assertFalse(validationService.validatePensionType(""));
        assertFalse(validationService.validatePensionType("XX"));
        assertFalse(validationService.validatePensionType("INVALID"));
    }

    @Test
    @DisplayName("기능코드 검증 - 유효한 코드")
    void validateFunctionCode_Valid() {
        // When & Then
        assertTrue(validationService.validateFunctionCode("A"));
        assertTrue(validationService.validateFunctionCode("F"));
        assertTrue(validationService.validateFunctionCode("R"));
        assertTrue(validationService.validateFunctionCode("I"));
        assertTrue(validationService.validateFunctionCode("V"));
        assertTrue(validationService.validateFunctionCode("X"));
    }

    @Test
    @DisplayName("기능코드 검증 - 무효한 코드")
    void validateFunctionCode_Invalid() {
        // When & Then
        assertFalse(validationService.validateFunctionCode(null));
        assertFalse(validationService.validateFunctionCode(""));
        assertFalse(validationService.validateFunctionCode("Z"));
        assertFalse(validationService.validateFunctionCode("ABC"));
    }

    @Test
    @DisplayName("등록코드 검증 - 유효한 코드")
    void validateRegCode_Valid() {
        // When & Then
        assertTrue(validationService.validateRegCode("AP"));
        assertTrue(validationService.validateRegCode("UP"));
        assertTrue(validationService.validateRegCode("EP"));
        assertTrue(validationService.validateRegCode("XX"));
    }

    @Test
    @DisplayName("등록코드 검증 - 무효한 코드")
    void validateRegCode_Invalid() {
        // When & Then
        assertFalse(validationService.validateRegCode(null));
        assertFalse(validationService.validateRegCode(""));
        assertFalse(validationService.validateRegCode("INVALID"));
        assertFalse(validationService.validateRegCode("ZZ"));
    }

    @Test
    @DisplayName("나이 계산 - 유효한 FNR")
    void calculateAge_ValidFNR() {
        // When
        int age = validationService.calculateAge("01010100131");

        // Then
        assertTrue(age > 0);
    }

    @Test
    @DisplayName("나이 계산 - 무효한 FNR")
    void calculateAge_InvalidFNR() {
        // When
        int age = validationService.calculateAge("12345");

        // Then
        assertEquals(-1, age);
    }

    @Test
    @DisplayName("성별 추출 - 남성")
    void getGender_Male() {
        // When
        String gender = validationService.getGender("01010100131");

        // Then
        assertEquals("M", gender);
    }

    @Test
    @DisplayName("성별 추출 - 여성")
    void getGender_Female() {
        // When
        String gender = validationService.getGender("01010100050");

        // Then
        assertEquals("F", gender);
    }

    @Test
    @DisplayName("성별 추출 - 무효한 FNR")
    void getGender_InvalidFNR() {
        // When
        String gender = validationService.getGender("12345");

        // Then
        assertNull(gender);
    }

    @Test
    @DisplayName("숫자 필드 검증 - 유효한 값")
    void validateNumericField_Valid() {
        // When
        String error = validationService.validateNumericField(50, "Age", 0, 150);

        // Then
        assertNull(error);
    }

    @Test
    @DisplayName("숫자 필드 검증 - null 값")
    void validateNumericField_Null() {
        // When
        String error = validationService.validateNumericField(null, "Age", 0, 150);

        // Then
        assertNotNull(error);
        assertTrue(error.contains("required"));
    }

    @Test
    @DisplayName("숫자 필드 검증 - 범위 초과")
    void validateNumericField_OutOfRange() {
        // When
        String error = validationService.validateNumericField(200, "Age", 0, 150);

        // Then
        assertNotNull(error);
        assertTrue(error.contains("between"));
    }

    @Test
    @DisplayName("문자열 필드 검증 - 유효한 값")
    void validateStringField_Valid() {
        // When
        String error = validationService.validateStringField("Ola", "Name", 50, true);

        // Then
        assertNull(error);
    }

    @Test
    @DisplayName("문자열 필드 검증 - null 값 (필수)")
    void validateStringField_NullRequired() {
        // When
        String error = validationService.validateStringField(null, "Name", 50, true);

        // Then
        assertNotNull(error);
        assertTrue(error.contains("required"));
    }

    @Test
    @DisplayName("문자열 필드 검증 - null 값 (선택)")
    void validateStringField_NullOptional() {
        // When
        String error = validationService.validateStringField(null, "Name", 50, false);

        // Then
        assertNull(error);
    }

    @Test
    @DisplayName("문자열 필드 검증 - 길이 초과")
    void validateStringField_TooLong() {
        // When
        String error = validationService.validateStringField("This is a very long name that exceeds the maximum length", "Name", 10, true);

        // Then
        assertNotNull(error);
        assertTrue(error.contains("exceeds"));
    }
}

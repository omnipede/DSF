package com.nav.dsf.domain.service;

import com.nav.dsf.domain.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DisabilityPensionService.
 */
class DisabilityPensionServiceTest {

    private DisabilityPensionService disabilityPensionService;
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
        disabilityPensionService = new DisabilityPensionService(validationService);
    }

    @Test
    @DisplayName("UP 등록 성공 - 유효한 데이터")
    void registerDisabilityPension_Success() {
        // Given
        DisabilityPensionService.UPRegistrationData data = new DisabilityPensionService.UPRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola Nordmann");
        data.setUforhetsgrad(100);
        data.setSykdomskode("DIABETES");
        data.setYrkeskode("1234");
        data.setGrunnpensjon(100000);
        data.setSaertekst("Test");

        Person person = new Person("01010100131", "Ola Nordmann");

        // When
        DisabilityPensionService.UPRegistrationResult result = disabilityPensionService.registerDisabilityPension(data, person);

        // Then
        assertTrue(result.isSuccess());
        assertNotNull(result.getPerson());
        assertNull(result.getErrorMessage());
        assertEquals(100, result.getPerson().getDisabilityPension().getUforhetsgrad());
    }

    @Test
    @DisplayName("UP 등록 실패 - 장애율 20% 미만")
    void registerDisabilityPension_UforhetsgradTooLow() {
        // Given
        DisabilityPensionService.UPRegistrationData data = new DisabilityPensionService.UPRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola");
        data.setUforhetsgrad(19);

        // When
        DisabilityPensionService.UPRegistrationResult result = disabilityPensionService.registerDisabilityPension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("UFØRHETSGRAD"));
    }

    @Test
    @DisplayName("UP 등록 실패 - 장애율 100% 초과")
    void registerDisabilityPension_UforhetsgradTooHigh() {
        // Given
        DisabilityPensionService.UPRegistrationData data = new DisabilityPensionService.UPRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola");
        data.setUforhetsgrad(101);

        // When
        DisabilityPensionService.UPRegistrationResult result = disabilityPensionService.registerDisabilityPension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("UFØRHETSGRAD"));
    }

    @Test
    @DisplayName("UP 등록 성공 - 장애율 100%")
    void registerDisabilityPension_Uforhetsgrad100() {
        // Given
        DisabilityPensionService.UPRegistrationData data = new DisabilityPensionService.UPRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola");
        data.setUforhetsgrad(100);

        // When
        DisabilityPensionService.UPRegistrationResult result = disabilityPensionService.registerDisabilityPension(data, null);

        // Then
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("UP 등록 성공 - 장애율 50%")
    void registerDisabilityPension_Uforhetsgrad50() {
        // Given
        DisabilityPensionService.UPRegistrationData data = new DisabilityPensionService.UPRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola");
        data.setUforhetsgrad(50);

        // When
        DisabilityPensionService.UPRegistrationResult result = disabilityPensionService.registerDisabilityPension(data, null);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(50, result.getPerson().getDisabilityPension().getUforhetsgrad());
    }

    @Test
    @DisplayName("장애율 자격 검사 - 20% 이상")
    void checkDisabilityEligibility_20OrHigher() {
        // When & Then
        assertTrue(disabilityPensionService.checkDisabilityEligibility(20));
        assertTrue(disabilityPensionService.checkDisabilityEligibility(50));
        assertTrue(disabilityPensionService.checkDisabilityEligibility(100));
    }

    @Test
    @DisplayName("장애율 자격 검사 - 20% 미만")
    void checkDisabilityEligibility_Under20() {
        // When & Then
        assertFalse(disabilityPensionService.checkDisabilityEligibility(0));
        assertFalse(disabilityPensionService.checkDisabilityEligibility(19));
    }

    @Test
    @DisplayName("장애율 상태 메시지 - 100%")
    void getEligibilityStatus_100Percent() {
        // When
        String status = disabilityPensionService.getEligibilityStatus(100);

        // Then
        assertTrue(status.contains("HELT UFØR"));
    }

    @Test
    @DisplayName("장애율 상태 메시지 - 50%")
    void getEligibilityStatus_50Percent() {
        // When
        String status = disabilityPensionService.getEligibilityStatus(50);

        // Then
        assertTrue(status.contains("DELVIS UFØR"));
    }

    @Test
    @DisplayName("장애율 상태 메시지 - 10%")
    void getEligibilityStatus_10Percent() {
        // When
        String status = disabilityPensionService.getEligibilityStatus(10);

        // Then
        assertTrue(status.contains("IKKE BERETTIGET"));
    }

    @Test
    @DisplayName("연금 계산 - 장애율 100%")
    void calculatePensionAmounts_Uforhetsgrad100() {
        // Given
        Person person = new Person("01010100131", "Ola");
        person.getDisabilityPension().setUforhetsgrad(100);

        // When
        disabilityPensionService.calculatePensionAmounts(person);

        // Then
        // G = 116011, uforhet factor = 1.0
        assertEquals(116011, person.getDisabilityPension().getGp());
        assertEquals(52204, person.getDisabilityPension().getTp());
    }

    @Test
    @DisplayName("연금 계산 - 장애율 50%")
    void calculatePensionAmounts_Uforhetsgrad50() {
        // Given
        Person person = new Person("01010100131", "Ola");
        person.getDisabilityPension().setUforhetsgrad(50);

        // When
        disabilityPensionService.calculatePensionAmounts(person);

        // Then
        // G = 116011, uforhet factor = 0.5
        assertEquals(58005, person.getDisabilityPension().getGp());
        assertEquals(26102, person.getDisabilityPension().getTp());
    }

    @Test
    @DisplayName("등록 데이터 생성 - 유효한 입력")
    void createRegistrationData_ValidInput() {
        // When
        DisabilityPensionService.UPRegistrationData data = disabilityPensionService.createRegistrationData(
            "01010100131", "Ola", "100", "DIABETES", "1234", "100000", "Test"
        );

        // Then
        assertEquals("01010100131", data.getFnr());
        assertEquals("Ola", data.getName());
        assertEquals(100, data.getUforhetsgrad());
        assertEquals("DIABETES", data.getSykdomskode());
        assertEquals("1234", data.getYrkeskode());
        assertEquals(100000, data.getGrunnpensjon());
        assertEquals("Test", data.getSaertekst());
    }

    @Test
    @DisplayName("등록 데이터 생성 - 숫자 변환 오류 처리")
    void createRegistrationData_InvalidNumbers() {
        // When
        DisabilityPensionService.UPRegistrationData data = disabilityPensionService.createRegistrationData(
            "01010100131", "Ola", "abc", "DIABETES", "1234", "xyz", null
        );

        // Then
        assertEquals(0, data.getUforhetsgrad());
        assertEquals(0, data.getGrunnpensjon());
    }
}

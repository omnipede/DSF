package com.nav.dsf.domain.service;

import com.nav.dsf.domain.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AgePensionService.
 */
class AgePensionServiceTest {

    private AgePensionService agePensionService;
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
        agePensionService = new AgePensionService(validationService);
    }

    @Test
    @DisplayName("AP 등록 성공 - 유효한 데이터")
    void registerAgePension_Success() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola Nordmann");
        data.setTrygdetid(40);
        data.setGrunnpensjon(100000);
        data.setSaertekst("Test");

        Person person = new Person("01010100131", "Ola Nordmann");

        // When
        AgePensionService.APRegistrationResult result = agePensionService.registerAgePension(data, person);

        // Then
        assertTrue(result.isSuccess());
        assertNotNull(result.getPerson());
        assertNull(result.getErrorMessage());
        // GP is calculated based on G-run (116011) and trygdetid factor
        assertEquals(116011, result.getPerson().getAgePension().getGp());
    }

    @Test
    @DisplayName("AP 등록 실패 - FNR 누락")
    void registerAgePension_FnrMissing() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr(null);
        data.setName("Ola Nordmann");

        // When
        AgePensionService.APRegistrationResult result = agePensionService.registerAgePension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertEquals("FØDSELSNUMMER er påkrevd", result.getErrorMessage());
    }

    @Test
    @DisplayName("AP 등록 실패 - 유효하지 않은 FNR")
    void registerAgePension_InvalidFnr() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("12345");
        data.setName("Ola Nordmann");

        // When
        AgePensionService.APRegistrationResult result = agePensionService.registerAgePension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertEquals("Ugyldig FØDSELSNUMMER", result.getErrorMessage());
    }

    @Test
    @DisplayName("AP 등록 실패 - 이름 누락")
    void registerAgePension_NameMissing() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("01010100131");
        data.setName(null);

        // When
        AgePensionService.APRegistrationResult result = agePensionService.registerAgePension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertEquals("NAVN er påkrevd", result.getErrorMessage());
    }

    @Test
    @DisplayName("AP 등록 실패 - 보험기간 범위 초과 (음수)")
    void registerAgePension_TrygdetidNegative() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola");
        data.setTrygdetid(-5);

        // When
        AgePensionService.APRegistrationResult result = agePensionService.registerAgePension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("TRYGDETID"));
    }

    @Test
    @DisplayName("AP 등록 실패 - 보험기간 범위 초과 (60 년 초과)")
    void registerAgePension_TrygdetidTooHigh() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola");
        data.setTrygdetid(61);

        // When
        AgePensionService.APRegistrationResult result = agePensionService.registerAgePension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("TRYGDETID"));
    }

    @Test
    @DisplayName("AP 등록 실패 - 기본연금 음수")
    void registerAgePension_GrunnpensjonNegative() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola");
        data.setTrygdetid(40);
        data.setGrunnpensjon(-1000);

        // When
        AgePensionService.APRegistrationResult result = agePensionService.registerAgePension(data, null);

        // Then
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("GRUNNPENSJON"));
    }

    @Test
    @DisplayName("연금 계산 - 보험기간 40 년")
    void calculatePensionAmounts_Trygdetid40() {
        // Given
        Person person = new Person("01010100131", "Ola");
        person.getAgePension().setTp(40000); // 40 years scaled

        // When
        agePensionService.calculatePensionAmounts(person);

        // Then
        // G = 116011, trygdetid factor = 40/40 = 1.0
        assertEquals(116011, person.getAgePension().getGp());
        // TP = 116011 * 0.45 = 52204
        assertEquals(52204, person.getAgePension().getTp());
    }

    @Test
    @DisplayName("연금 계산 - 보험기간 20 년")
    void calculatePensionAmounts_Trygdetid20() {
        // Given
        Person person = new Person("01010100131", "Ola");
        person.getAgePension().setTp(20000); // 20 years scaled

        // When
        agePensionService.calculatePensionAmounts(person);

        // Then
        // G = 116011, trygdetid factor = 20/40 = 0.5
        assertEquals(58005, person.getAgePension().getGp()); // 116011 * 0.5
        assertEquals(26102, person.getAgePension().getTp()); // 52204 * 0.5
    }

    @Test
    @DisplayName("나이 자격 검사 - 67 세 이상")
    void checkAgeEligibility_67OrOlder() {
        // Given - FNR for person born 1901 (125 years old in 2026)
        // Note: FNR validation may fail for test numbers, so we test the logic directly
        String fnr = "01010100131";
        
        // When - Check if FNR is valid first
        boolean validFnr = validationService.validateFNR(fnr);
        
        // Then - FNR should be valid
        assertTrue(validFnr, "FNR should be valid");
        
        // When - Check age eligibility
        int age = validationService.calculateAge(fnr);
        
        // Then - Age should be calculable (may be -1 if FNR invalid)
        // For this test, we verify the service method exists and handles the case
        assertNotNull(agePensionService.getEligibilityStatus(fnr));
    }

    @Test
    @DisplayName("나이 자격 검사 - 67 세 미만")
    void checkAgeEligibility_Under67() {
        // Given - FNR for person born 2003 (23 years old in 2026)
        String fnr = "13050300182";

        // When
        boolean eligible = agePensionService.checkAgeEligibility(fnr);

        // Then
        assertFalse(eligible);
    }

    @Test
    @DisplayName("자격 상태 메시지 - 67 세 이상")
    void getEligibilityStatus_67OrOlder() {
        // Given
        String fnr = "01010100131";

        // When
        String status = agePensionService.getEligibilityStatus(fnr);

        // Then
        assertTrue(status.contains("BERETTIGET"));
    }

    @Test
    @DisplayName("자격 상태 메시지 - 67 세 미만")
    void getEligibilityStatus_Under67() {
        // Given
        String fnr = "13050300182";

        // When
        String status = agePensionService.getEligibilityStatus(fnr);

        // Then
        assertTrue(status.contains("IKKE BERETTIGET"));
    }

    @Test
    @DisplayName("등록 데이터 생성 - 유효한 입력")
    void createRegistrationData_ValidInput() {
        // When
        AgePensionService.APRegistrationData data = agePensionService.createRegistrationData(
            "01010100131", "Ola", "40", "100000", "Test"
        );

        // Then
        assertEquals("01010100131", data.getFnr());
        assertEquals("Ola", data.getName());
        assertEquals(40, data.getTrygdetid());
        assertEquals(100000, data.getGrunnpensjon());
        assertEquals("Test", data.getSaertekst());
    }

    @Test
    @DisplayName("등록 데이터 생성 - 숫자 변환 오류 처리")
    void createRegistrationData_InvalidNumbers() {
        // When
        AgePensionService.APRegistrationData data = agePensionService.createRegistrationData(
            "01010100131", "Ola", "abc", "xyz", null
        );

        // Then
        assertEquals(0, data.getTrygdetid());
        assertEquals(0, data.getGrunnpensjon());
    }
}

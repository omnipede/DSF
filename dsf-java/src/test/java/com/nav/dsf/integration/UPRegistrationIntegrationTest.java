package com.nav.dsf.integration;

import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.model.Transaction;
import com.nav.dsf.domain.repository.MemoryDatabase;
import com.nav.dsf.domain.repository.TransactionRepository;
import com.nav.dsf.domain.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UP (Disability Pension) Registration.
 */
class UPRegistrationIntegrationTest {

    private DisabilityPensionService disabilityPensionService;
    private TransactionService transactionService;
    private PersonService personService;
    private MemoryDatabase database;
    private TransactionRepository txRepository;

    @BeforeEach
    void setUp() {
        database = new MemoryDatabase();
        txRepository = new TransactionRepository();
        ValidationService validationService = new ValidationService();
        disabilityPensionService = new DisabilityPensionService(validationService);
        personService = new PersonService(database);
        transactionService = new TransactionService(txRepository, database);
    }

    @Test
    @DisplayName("UP 등록 전체 플로우 - 성공")
    void UPRegistration_FullFlow_Success() {
        // Given - Registration data
        DisabilityPensionService.UPRegistrationData data = new DisabilityPensionService.UPRegistrationData();
        data.setFnr("01010100131");
        data.setName("Per Hansen");
        data.setUforhetsgrad(100);
        data.setSykdomskode("DIABETES");
        data.setYrkeskode("1234");
        data.setGrunnpensjon(100000);
        data.setSaertekst("Test");

        // When - Register UP
        Person person = new Person(data.getFnr(), data.getName());
        DisabilityPensionService.UPRegistrationResult upResult = disabilityPensionService.registerDisabilityPension(data, person);

        // Then - UP registration successful
        assertTrue(upResult.isSuccess());
        assertNotNull(upResult.getPerson());
        
        // When - Create and process transaction
        Transaction transaction = transactionService.createTransaction(
            data.getFnr(), "R060", "UP", "DEMO", data
        );
        TransactionService.TransactionResult txResult = transactionService.processTransaction(transaction);

        // Then - Transaction successful
        assertTrue(txResult.isSuccess());
        assertEquals(Transaction.Status.PROCESSED, txResult.getTransaction().getStatus());
        
        // Verify person saved in database
        assertTrue(database.exists(data.getFnr()));
        
        // Verify pension data
        Person savedPerson = database.findByFnr(data.getFnr()).get();
        assertEquals("UP", savedPerson.getPensionStatus().getPensionType1());
        assertEquals(100, savedPerson.getDisabilityPension().getUforhetsgrad());
        assertEquals(116011, savedPerson.getDisabilityPension().getGp()); // Calculated GP
    }

    @Test
    @DisplayName("UP 등록 - 자격 검사 실패 (장애율 20% 미만)")
    void UPRegistration_DisabilityEligibility_Fail() {
        // Given - Low disability grade
        DisabilityPensionService.UPRegistrationData data = new DisabilityPensionService.UPRegistrationData();
        data.setFnr("01010100131");
        data.setName("Per");
        data.setUforhetsgrad(19);

        // When - Check eligibility
        boolean eligible = disabilityPensionService.checkDisabilityEligibility(19);

        // Then - Not eligible
        assertFalse(eligible);
        
        // When - Try to register
        Person person = new Person(data.getFnr(), data.getName());
        DisabilityPensionService.UPRegistrationResult result = disabilityPensionService.registerDisabilityPension(data, person);

        // Then - Registration failed
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("UFØRHETSGRAD"));
    }

    @Test
    @DisplayName("UP 등록 - 장애율별 연금 계산")
    void UPRegistration_PensionCalculation() {
        // Given - Different disability grades
        Person person100 = new Person("01010100131", "Per 100%");
        person100.getDisabilityPension().setUforhetsgrad(100);
        
        Person person50 = new Person("13050300182", "Per 50%");
        person50.getDisabilityPension().setUforhetsgrad(50);

        // When - Calculate pensions
        disabilityPensionService.calculatePensionAmounts(person100);
        disabilityPensionService.calculatePensionAmounts(person50);

        // Then - 100% gets full pension
        assertEquals(116011, person100.getDisabilityPension().getGp());
        assertEquals(52204, person100.getDisabilityPension().getTp());
        
        // Then - 50% gets half pension
        assertEquals(58005, person50.getDisabilityPension().getGp());
        assertEquals(26102, person50.getDisabilityPension().getTp());
    }
}

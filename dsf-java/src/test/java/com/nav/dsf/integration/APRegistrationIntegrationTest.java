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
 * Integration tests for AP (Age Pension) Registration.
 * Tests the complete flow from data validation to database persistence.
 */
class APRegistrationIntegrationTest {

    private AgePensionService agePensionService;
    private TransactionService transactionService;
    private PersonService personService;
    private MemoryDatabase database;
    private TransactionRepository txRepository;

    @BeforeEach
    void setUp() {
        database = new MemoryDatabase();
        txRepository = new TransactionRepository();
        ValidationService validationService = new ValidationService();
        agePensionService = new AgePensionService(validationService);
        personService = new PersonService(database);
        transactionService = new TransactionService(txRepository, database);
    }

    @Test
    @DisplayName("AP 등록 전체 플로우 - 성공")
    void APRegistration_FullFlow_Success() {
        // Given - Registration data
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola Nordmann");
        data.setTrygdetid(40);
        data.setGrunnpensjon(100000);
        data.setSaertekst("Test");

        // When - Register AP
        Person person = new Person(data.getFnr(), data.getName());
        AgePensionService.APRegistrationResult apResult = agePensionService.registerAgePension(data, person);

        // Then - AP registration successful
        assertTrue(apResult.isSuccess());
        assertNotNull(apResult.getPerson());
        
        // When - Create and process transaction
        Transaction transaction = transactionService.createTransaction(
            data.getFnr(), "R050", "AP", "DEMO", data
        );
        TransactionService.TransactionResult txResult = transactionService.processTransaction(transaction);

        // Then - Transaction successful
        assertTrue(txResult.isSuccess());
        assertEquals(Transaction.Status.PROCESSED, txResult.getTransaction().getStatus());
        
        // Verify person saved in database
        assertTrue(database.exists(data.getFnr()));
        
        // Verify pension data
        Person savedPerson = database.findByFnr(data.getFnr()).get();
        assertEquals("AP", savedPerson.getPensionStatus().getPensionType1());
        assertEquals(116011, savedPerson.getAgePension().getGp()); // Calculated GP
    }

    @Test
    @DisplayName("AP 등록 - 자격 검사 실패 (67 세 미만)")
    void APRegistration_AgeEligibility_Fail() {
        // Given - Young person (born 2003, 23 years old)
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("13050300182");
        data.setName("Per Hansen");
        data.setTrygdetid(5);
        data.setGrunnpensjon(50000);

        // When - Check eligibility
        boolean eligible = agePensionService.checkAgeEligibility(data.getFnr());

        // Then - Not eligible
        assertFalse(eligible);
    }

    @Test
    @DisplayName("AP 등록 - 트랜잭션 이력 확인")
    void APRegistration_TransactionHistory() {
        // Given - Multiple AP registrations
        AgePensionService.APRegistrationData data1 = new AgePensionService.APRegistrationData();
        data1.setFnr("01010100131");
        data1.setName("Ola");
        data1.setTrygdetid(40);
        data1.setGrunnpensjon(100000);

        AgePensionService.APRegistrationData data2 = new AgePensionService.APRegistrationData();
        data2.setFnr("01010100131");
        data2.setName("Ola Updated");
        data2.setTrygdetid(41);
        data2.setGrunnpensjon(110000);

        // When - Process two transactions
        Transaction tx1 = transactionService.createTransaction(data1.getFnr(), "R050", "AP", "DEMO", data1);
        Transaction tx2 = transactionService.createTransaction(data2.getFnr(), "R050", "AP", "DEMO", data2);
        
        transactionService.processTransaction(tx1);
        transactionService.processTransaction(tx2);

        // Then - Check history
        var history = transactionService.getTransactionHistory("01010100131");
        assertEquals(2, history.size());
    }

    @Test
    @DisplayName("AP 등록 - JSON 저장 확인")
    void APRegistration_JsonSave() {
        // Given
        AgePensionService.APRegistrationData data = new AgePensionService.APRegistrationData();
        data.setFnr("01010100131");
        data.setName("Ola Nordmann");
        data.setTrygdetid(40);
        data.setGrunnpensjon(100000);

        // When - Register
        Person person = new Person(data.getFnr(), data.getName());
        agePensionService.registerAgePension(data, person);

        // Then - Verify data in memory database
        assertTrue(database.exists(data.getFnr()));
        Person savedPerson = database.findByFnr(data.getFnr()).get();
        assertEquals("Ola Nordmann", savedPerson.getName());
        assertNotNull(savedPerson.getAgePension());
    }
}

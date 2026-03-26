package com.nav.dsf.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Transaction.
 */
class TransactionTest {

    @Test
    @DisplayName("Transaction 생성자 - 초기값")
    void constructor_DefaultValues() {
        // When
        Transaction tx = new Transaction("01010100131", "R050", "AP", "DEMO");

        // Then
        assertNotNull(tx);
        assertEquals("01010100131", tx.getFnr());
        assertEquals("R050", tx.getTransCode());
        assertEquals("AP", tx.getBlankettType());
        assertEquals("DEMO", tx.getCaseWorker());
        assertEquals(Transaction.Status.PENDING, tx.getStatus());
        assertNotNull(tx.getTransactionDate());
    }

    @Test
    @DisplayName("ID 설정 및 가져오기")
    void setIdAndGet() {
        // Given
        Transaction tx = new Transaction();

        // When
        tx.setId("TX-TEST123");

        // Then
        assertEquals("TX-TEST123", tx.getId());
    }

    @Test
    @DisplayName("상태 설정 및 가져오기")
    void setStatusAndGet() {
        // Given
        Transaction tx = new Transaction();

        // When
        tx.setStatus(Transaction.Status.PROCESSED);

        // Then
        assertEquals(Transaction.Status.PROCESSED, tx.getStatus());
    }

    @Test
    @DisplayName("상태 설정 및 가져오기 - FAILED")
    void setStatus_Failed() {
        // Given
        Transaction tx = new Transaction();

        // When
        tx.setStatus(Transaction.Status.FAILED);
        tx.setErrorMessage("Test error");

        // Then
        assertEquals(Transaction.Status.FAILED, tx.getStatus());
        assertEquals("Test error", tx.getErrorMessage());
    }

    @Test
    @DisplayName("데이터 설정 및 가져오기")
    void setDataAndGet() {
        // Given
        Transaction tx = new Transaction();
        Object testData = new Object();

        // When
        tx.setData(testData);

        // Then
        assertEquals(testData, tx.getData());
    }

    @Test
    @DisplayName("toString - null 아님")
    void toString_NotNull() {
        // Given
        Transaction tx = new Transaction("01010100131", "R050", "AP", "DEMO");
        tx.setId("TX-TEST");

        // When
        String str = tx.toString();

        // Then
        assertNotNull(str);
        assertTrue(str.contains("TX-TEST"));
        assertTrue(str.contains("01010100131"));
    }
}

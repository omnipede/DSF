package com.nav.dsf.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Transaction record for DSF.
 * Represents a single transaction in the transaction history.
 * 
 * Original PL/I structure from TRANS_OPPL_OMR:
 * - FØDSNUMMER (11 digits)
 * - TRANSAKSJONSKODE (4 chars)
 * - BLANKETTYPE (2 chars)
 * - VIRK_DATO (effective date)
 * - SAKSBEHANDLER (case worker)
 * - DATO (transaction date)
 * - STATUS (transaction status)
 */
public class Transaction {
    
    /**
     * Transaction status codes.
     */
    public enum Status {
        PENDING,    // Ventetrans - waiting to be processed
        PROCESSED,  // Ferdig - successfully processed
        FAILED,     // Feil - processing failed
        ROLLED_BACK // Rullet tilbake - rolled back
    }
    
    private String id;                    // Unique transaction ID
    private String fnr;                   // Fødselsnummer
    private String transCode;             // Transaksjonskode (R050, R060, etc.)
    private String blankettType;          // Blankettype (AP, UP, EP, etc.)
    private LocalDate effectiveDate;      // Virkningsdato
    private String caseWorker;            // Saksbehandler ID
    private LocalDateTime transactionDate; // Transaksjonsdato
    private Status status;                // Transaksjonsstatus
    private String pensionType;           // Pensjonstype (AP, UP, etc.)
    private Object data;                  // Transaction data (APRegistrationData, etc.)
    private String errorMessage;          // Feilmelding hvis failed
    private String originalData;          // Original data for rollback
    private LocalDateTime rollbackDate;   // Rullegbackdato

    public Transaction() {
        this.transactionDate = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    public Transaction(String fnr, String transCode, String blankettType, String caseWorker) {
        this();
        this.fnr = fnr;
        this.transCode = transCode;
        this.blankettType = blankettType;
        this.caseWorker = caseWorker;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getBlankettType() {
        return blankettType;
    }

    public void setBlankettType(String blankettType) {
        this.blankettType = blankettType;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getCaseWorker() {
        return caseWorker;
    }

    public void setCaseWorker(String caseWorker) {
        this.caseWorker = caseWorker;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPensionType() {
        return pensionType;
    }

    public void setPensionType(String pensionType) {
        this.pensionType = pensionType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOriginalData() {
        return originalData;
    }

    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }

    public LocalDateTime getRollbackDate() {
        return rollbackDate;
    }

    public void setRollbackDate(LocalDateTime rollbackDate) {
        this.rollbackDate = rollbackDate;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%s, fnr=%s, transCode=%s, status=%s, date=%s}",
            id, fnr, transCode, status, transactionDate);
    }
}

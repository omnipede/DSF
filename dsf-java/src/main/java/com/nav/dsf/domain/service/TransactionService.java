package com.nav.dsf.domain.service;

import com.nav.dsf.common.util.FNRValidator;
import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.model.Transaction;
import com.nav.dsf.domain.repository.MemoryDatabase;
import com.nav.dsf.domain.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Transaction processing service.
 * Implements R0012001 functionality - transaction validation and processing.
 * 
 * Original PL/I program R0012001 handles:
 * - Transaction validation
 * - Data consistency checks
 * - Database updates with rollback support
 * - Transaction logging
 */
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MemoryDatabase personDatabase;
    private final List<Transaction> pendingTransactions;

    public TransactionService(TransactionRepository transactionRepository, MemoryDatabase personDatabase) {
        this.transactionRepository = transactionRepository;
        this.personDatabase = personDatabase;
        this.pendingTransactions = new ArrayList<>();
    }

    /**
     * Transaction processing result.
     */
    public static class TransactionResult {
        private final boolean success;
        private final Transaction transaction;
        private final String errorMessage;

        public TransactionResult(boolean success, Transaction transaction, String errorMessage) {
            this.success = success;
            this.transaction = transaction;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() { return success; }
        public Transaction getTransaction() { return transaction; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * Creates a new transaction.
     */
    public Transaction createTransaction(String fnr, String transCode, String blankettType, 
                                         String caseWorker, Object data) {
        Transaction transaction = new Transaction(fnr, transCode, blankettType, caseWorker);
        transaction.setId(generateTransactionId());
        transaction.setData(data);
        transaction.setPensionType(blankettType);
        return transaction;
    }

    /**
     * Validates a transaction before processing.
     * R0012001 validation logic.
     */
    public String validateTransaction(Transaction transaction) {
        if (transaction == null) {
            return "Transaksjonen mangler";
        }

        // Validate FNR
        if (transaction.getFnr() == null || transaction.getFnr().trim().isEmpty()) {
            return "FØDSELSNUMMER er påkrevd";
        }

        if (!FNRValidator.isValid(transaction.getFnr())) {
            return "Ugyldig FØDSELSNUMMER";
        }

        // Validate transaction code
        if (transaction.getTransCode() == null || transaction.getTransCode().trim().isEmpty()) {
            return "TRANSAKSJONSKODE er påkrevd";
        }

        // Validate blankett type
        if (transaction.getBlankettType() == null || transaction.getBlankettType().trim().isEmpty()) {
            return "BLANKETTYPE er påkrevd";
        }

        // Validate case worker
        if (transaction.getCaseWorker() == null || transaction.getCaseWorker().trim().isEmpty()) {
            return "SAKSBEHANDLER er påkrevd";
        }

        // Check if person exists (for registration transactions)
        if (transaction.getTransCode().startsWith("R")) {
            Optional<Person> personOpt = personDatabase.findByFnr(transaction.getFnr());
            // Person may not exist for new registrations - this is OK
        }

        return null; // Valid
    }

    /**
     * Processes a single transaction.
     * R0012001 main processing logic.
     */
    public TransactionResult processTransaction(Transaction transaction) {
        // Step 1: Validate transaction
        String validationError = validateTransaction(transaction);
        if (validationError != null) {
            transaction.setStatus(Transaction.Status.FAILED);
            transaction.setErrorMessage(validationError);
            return new TransactionResult(false, transaction, validationError);
        }

        // Step 2: Save original data for rollback
        Optional<Person> existingPerson = personDatabase.findByFnr(transaction.getFnr());
        if (existingPerson.isPresent()) {
            transaction.setOriginalData(serializePerson(existingPerson.get()));
        }

        // Step 3: Process based on transaction type
        boolean processed = false;
        try {
            processed = executeTransaction(transaction);
        } catch (Exception e) {
            transaction.setStatus(Transaction.Status.FAILED);
            transaction.setErrorMessage("Behandlingsfeil: " + e.getMessage());
            return new TransactionResult(false, transaction, e.getMessage());
        }

        if (!processed) {
            transaction.setStatus(Transaction.Status.FAILED);
            transaction.setErrorMessage("Ukjent transaksjonstype: " + transaction.getTransCode());
            return new TransactionResult(false, transaction, transaction.getErrorMessage());
        }

        // Step 4: Mark as processed
        transaction.setStatus(Transaction.Status.PROCESSED);
        transaction.setTransactionDate(LocalDateTime.now());

        // Step 5: Save to repository
        transactionRepository.save(transaction);

        return new TransactionResult(true, transaction, null);
    }

    /**
     * Executes the transaction based on type.
     */
    private boolean executeTransaction(Transaction transaction) {
        String transCode = transaction.getTransCode();
        String blankettType = transaction.getBlankettType();

        // Route to appropriate handler
        if (transCode.startsWith("R05") || blankettType.equals("AP")) {
            return processAPTransaction(transaction);
        } else if (transCode.startsWith("R06") || blankettType.equals("UP")) {
            return processUPTransaction(transaction);
        } else if (transCode.startsWith("R08") || blankettType.equals("EP")) {
            return processEPTransaction(transaction);
        }
        // Add more handlers as implemented

        return false; // Unknown transaction type
    }

    /**
     * Processes AP (Age Pension) transaction.
     */
    private boolean processAPTransaction(Transaction transaction) {
        Object data = transaction.getData();
        if (!(data instanceof AgePensionService.APRegistrationData)) {
            return false;
        }

        AgePensionService.APRegistrationData apData = (AgePensionService.APRegistrationData) data;
        
        // Get or create person
        Optional<Person> personOpt = personDatabase.findByFnr(transaction.getFnr());
        Person person;
        
        if (personOpt.isPresent()) {
            person = personOpt.get();
        } else {
            person = new Person(transaction.getFnr(), apData.getName());
        }

        // Use AgePensionService to register
        AgePensionService apService = new AgePensionService(new ValidationService());
        AgePensionService.APRegistrationResult result = apService.registerAgePension(apData, person);

        if (!result.isSuccess()) {
            return false;
        }

        // Save to database
        personDatabase.save(result.getPerson());
        return true;
    }

    /**
     * Processes UP (Disability Pension) transaction.
     */
    private boolean processUPTransaction(Transaction transaction) {
        Object data = transaction.getData();
        if (!(data instanceof DisabilityPensionService.UPRegistrationData)) {
            return false;
        }

        DisabilityPensionService.UPRegistrationData upData = (DisabilityPensionService.UPRegistrationData) data;
        
        // Get or create person
        Optional<Person> personOpt = personDatabase.findByFnr(transaction.getFnr());
        Person person;
        
        if (personOpt.isPresent()) {
            person = personOpt.get();
        } else {
            person = new Person(transaction.getFnr(), upData.getName());
        }

        // Use DisabilityPensionService to register
        DisabilityPensionService upService = new DisabilityPensionService(new ValidationService());
        DisabilityPensionService.UPRegistrationResult result = upService.registerDisabilityPension(upData, person);

        if (!result.isSuccess()) {
            return false;
        }

        // Save to database
        personDatabase.save(result.getPerson());
        return true;
    }

    /**
     * Processes EP (Survivor Spouse) transaction.
     */
    private boolean processEPTransaction(Transaction transaction) {
        // TODO: Implement EP transaction processing
        return false;
    }

    /**
     * Processes multiple transactions as a batch.
     */
    public List<TransactionResult> processBatch(List<Transaction> transactions) {
        List<TransactionResult> results = new ArrayList<>();
        
        for (Transaction transaction : transactions) {
            TransactionResult result = processTransaction(transaction);
            results.add(result);
            
            if (!result.isSuccess()) {
                // Stop on first failure (can be configured)
                break;
            }
        }
        
        return results;
    }

    /**
     * Rolls back a transaction.
     * R0012001 rollback logic.
     */
    public boolean rollbackTransaction(String transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        
        if (!transactionOpt.isPresent()) {
            return false;
        }

        Transaction transaction = transactionOpt.get();
        
        if (transaction.getStatus() != Transaction.Status.PROCESSED) {
            return false; // Can only rollback processed transactions
        }

        try {
            // Restore original data
            if (transaction.getOriginalData() != null && transaction.getFnr() != null) {
                Person originalPerson = deserializePerson(transaction.getOriginalData());
                if (originalPerson != null) {
                    personDatabase.save(originalPerson);
                }
            }

            // Mark as rolled back
            transaction.setStatus(Transaction.Status.ROLLED_BACK);
            transaction.setRollbackDate(LocalDateTime.now());
            transactionRepository.save(transaction);

            return true;
        } catch (Exception e) {
            transaction.setErrorMessage("Tilbakerulling feilet: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets transaction history for a person.
     */
    public List<Transaction> getTransactionHistory(String fnr) {
        return transactionRepository.findByFnr(fnr);
    }

    /**
     * Gets pending transactions.
     */
    public List<Transaction> getPendingTransactions() {
        return new ArrayList<>(pendingTransactions);
    }

    /**
     * Generates a unique transaction ID.
     */
    private String generateTransactionId() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Serializes person to string for storage.
     */
    private String serializePerson(Person person) {
        // Simple serialization - in production, use JSON
        return String.format("%s|%s|%d|%d",
            person.getFnr(),
            person.getName(),
            person.getAgePension().getGp(),
            person.getAgePension().getTp()
        );
    }

    /**
     * Deserializes person from string.
     */
    private Person deserializePerson(String data) {
        if (data == null || data.trim().isEmpty()) {
            return null;
        }

        try {
            String[] parts = data.split("\\|");
            if (parts.length >= 4) {
                Person person = new Person(parts[0], parts[1]);
                person.getAgePension().setGp(Integer.parseInt(parts[2]));
                person.getAgePension().setTp(Integer.parseInt(parts[3]));
                return person;
            }
        } catch (Exception e) {
            // Ignore deserialization errors
        }
        return null;
    }
}

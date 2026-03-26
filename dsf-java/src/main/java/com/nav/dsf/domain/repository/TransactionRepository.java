package com.nav.dsf.domain.repository;

import com.nav.dsf.domain.model.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for transactions.
 * Stores and retrieves transaction records.
 */
public class TransactionRepository {

    private final Map<String, Transaction> transactions;

    public TransactionRepository() {
        this.transactions = new ConcurrentHashMap<>();
    }

    /**
     * Saves a transaction.
     */
    public Transaction save(Transaction transaction) {
        if (transaction == null || transaction.getId() == null) {
            throw new IllegalArgumentException("Transaction and ID cannot be null");
        }
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    /**
     * Finds a transaction by ID.
     */
    public Optional<Transaction> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(transactions.get(id));
    }

    /**
     * Finds all transactions for a specific FNR.
     */
    public List<Transaction> findByFnr(String fnr) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactions.values()) {
            if (fnr.equals(tx.getFnr())) {
                result.add(tx);
            }
        }
        return result;
    }

    /**
     * Finds transactions by status.
     */
    public List<Transaction> findByStatus(Transaction.Status status) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactions.values()) {
            if (tx.getStatus() == status) {
                result.add(tx);
            }
        }
        return result;
    }

    /**
     * Finds pending transactions.
     */
    public List<Transaction> findPending() {
        return findByStatus(Transaction.Status.PENDING);
    }

    /**
     * Deletes a transaction by ID.
     */
    public boolean delete(String id) {
        return transactions.remove(id) != null;
    }

    /**
     * Gets all transactions.
     */
    public List<Transaction> getAll() {
        return new ArrayList<>(transactions.values());
    }

    /**
     * Gets the count of transactions.
     */
    public int count() {
        return transactions.size();
    }

    /**
     * Clears all transactions (for testing).
     */
    public void clear() {
        transactions.clear();
    }
}

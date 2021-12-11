package com.company.transactionservice.repository;

import com.company.transactionservice.domain.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private static final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Iterable<Transaction> findAll() {
        return transactions.values();
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return existsById(id) ? Optional.of(transactions.get(id)) : Optional.empty();
    }

    @Override
    public boolean existsById(UUID id) {
        return transactions.get(id) != null;
    }

    @Override
    public Transaction save(Transaction transaction) {
        transactions.put(transaction.id(), transaction);
        return transaction;
    }

    @Override
    public void deleteById(UUID id) {
        transactions.remove(id);
    }
}

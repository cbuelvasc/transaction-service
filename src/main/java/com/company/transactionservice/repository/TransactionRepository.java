package com.company.transactionservice.repository;

import com.company.transactionservice.domain.Transaction;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    Iterable<Transaction> findAll();

    Optional<Transaction> findById(UUID id);

    boolean existsById(UUID id);

    Transaction save(Transaction transaction);

    void deleteById(UUID id);
}

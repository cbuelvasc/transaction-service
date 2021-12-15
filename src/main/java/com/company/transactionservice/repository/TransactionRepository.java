package com.company.transactionservice.repository;

import com.company.transactionservice.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByOriginAccount(String accountNumber);

    Page<Transaction> findByOriginAccount(Pageable pageable, String accountNumber);

    List<Transaction> findByOriginAccountOrderByCreatedDate(String accountNumber);
}

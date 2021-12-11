package com.company.transactionservice.demo;

import com.company.transactionservice.domain.Transaction;
import com.company.transactionservice.repository.TransactionRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("dev")
public class TransactionDataLoader {

    private final TransactionRepository transactionRepository;

    public TransactionDataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        var book1 = new Transaction(UUID.randomUUID(), 100.0, 1.0, "USD", "00001","00002","Pay data");
        var book2 = new Transaction(UUID.randomUUID(), 200.0, 2.0, "USD", "00002", "00001","Pay data");
        transactionRepository.save(book1);
        transactionRepository.save(book2);
    }
}

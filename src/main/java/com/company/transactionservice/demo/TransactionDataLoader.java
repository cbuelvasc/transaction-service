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
        var transactionOne = new Transaction();
        transactionOne.setId(UUID.randomUUID());
        transactionOne.setAmount(100.0);
        transactionOne.setTaxCollected(1.0);
        transactionOne.setCurrency("USD");
        transactionOne.setOriginAccount("00001");
        transactionOne.setDestinationAccount("00002");
        transactionOne.setDescription("Pay data");
        //transactionRepository.save(transactionOne);

        var transactionTwo = new Transaction();
        transactionTwo.setId(UUID.randomUUID());
        transactionTwo.setAmount(200.0);
        transactionTwo.setTaxCollected(2.0);
        transactionTwo.setCurrency("USD");
        transactionTwo.setOriginAccount("00002");
        transactionTwo.setDestinationAccount("00001");
        transactionTwo.setDescription("Pay data");
        //transactionRepository.save(transactionTwo);

    }
}

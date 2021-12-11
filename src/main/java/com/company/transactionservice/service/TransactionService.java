package com.company.transactionservice.service;

import com.company.transactionservice.domain.Transaction;
import com.company.transactionservice.repository.TransactionRepository;
import com.company.transactionservice.service.dto.TransactionDTO;
import com.company.transactionservice.service.exception.AlreadyExistsException;
import com.company.transactionservice.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Iterable<Transaction> viewTransactionList() {
        return transactionRepository.findAll();
    }

    public Transaction viewTransactionDetails(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("The transaction with id %s was not found.", id)));
    }

    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        if (transactionRepository.existsById(transactionDTO.getId())) {
            throw new AlreadyExistsException(String.format("A transaction with id %s already exists.", transactionDTO.getId()));
        }
        var transaction = new Transaction(
                transactionDTO.getId(),
                transactionDTO.getAmount(),
                transactionDTO.getTaxCollected(),
                transactionDTO.getCurrency(),
                transactionDTO.getOriginAccount(),
                transactionDTO.getDestinationAccount(),
                transactionDTO.getDescription());
        return new TransactionDTO(transactionRepository.save(transaction));
    }

    public void removeTransaction(UUID id) {
        if (!transactionRepository.existsById(id)) {
            throw new NotFoundException(String.format("The transaction with id %s was not found.", id));
        }
        transactionRepository.deleteById(id);
    }

    public TransactionDTO editTransactionDetails(UUID id, TransactionDTO transactionDTO) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isEmpty()) {
            return createTransaction(transactionDTO);
        }
        var transactionToUpdate = new Transaction(
                existingTransaction.get().id(),
                transactionDTO.getAmount(),
                transactionDTO.getTaxCollected(),
                transactionDTO.getCurrency(),
                transactionDTO.getOriginAccount(),
                transactionDTO.getDestinationAccount(),
                transactionDTO.getDescription());
        return new TransactionDTO(transactionRepository.save(transactionToUpdate));
    }
}

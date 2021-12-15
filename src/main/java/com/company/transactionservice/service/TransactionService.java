package com.company.transactionservice.service;

import com.company.transactionservice.domain.Account;
import com.company.transactionservice.repository.AccountRepository;
import com.company.transactionservice.domain.Transaction;
import com.company.transactionservice.repository.TransactionRepository;
import com.company.transactionservice.service.dto.TransactionDTO;
import com.company.transactionservice.service.exception.ExceedLimitException;
import com.company.transactionservice.service.exception.InsufficientFoundsException;
import com.company.transactionservice.service.exception.NotFoundException;
import com.company.transactionservice.service.exception.NotSupportedCurrencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static com.company.transactionservice.service.Operation.ACCREDIT;
import static com.company.transactionservice.service.Operation.DEBIT;

@Service
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public Page<TransactionDTO> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(TransactionDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<TransactionDTO> getAllTransactionsByOriginAccount(Pageable pageable, String originAccount) {
        accountRepository
                .findByAccountNumber(originAccount)
                .orElseThrow(() -> new NotFoundException(String.format("Not found origin account with id: %s", originAccount)));

        return transactionRepository.findByOriginAccount(pageable, originAccount).map(TransactionDTO::new);
    }

    @Transactional(readOnly = true)
    public TransactionDTO getTransaction(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found transaction with id: %s", id)));
        return new TransactionDTO(transaction);
    }

    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        if (!transactionDTO.getCurrency().equals("USD")) {
            throw new NotSupportedCurrencyException(String.format("Currency not supported for %s", transactionDTO.getCurrency()));
        }

        var destinationAccount = accountRepository
                .findByAccountNumber(transactionDTO.getDestinationAccount())
                .orElseThrow(() -> new NotFoundException(String.format("Not found destination account with id: %s", transactionDTO.getDestinationAccount())));

        var originAccount = accountRepository
                .findByAccountNumber(transactionDTO.getOriginAccount())
                .orElseThrow(() -> new NotFoundException(String.format("Not found origin account with id: %s", transactionDTO.getDestinationAccount())));

        if (originAccount.getCurrentBalance() <= (transactionDTO.getAmount() + calculateTax(transactionDTO.getAmount()))) {
            throw new InsufficientFoundsException("insufficient-founds");
        }

        var count = (int) transactionRepository.findByOriginAccountOrderByCreatedDate(transactionDTO.getOriginAccount()).stream()
                .filter(transaction -> transaction.getCreatedDate().format(dateTimeFormatter).equals(LocalDateTime.now().format(dateTimeFormatter)))
                .count();

        if (count >= 3) {
            throw new ExceedLimitException("Exceed the limit number of transfers allowed by day - max limit 3 per day");
        }

        var transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTaxCollected(this.calculateTax(transactionDTO.getAmount()));
        transaction.setCurrency(transactionDTO.getCurrency());
        transaction.setOriginAccount(transactionDTO.getOriginAccount());
        transaction.setDestinationAccount(transactionDTO.getDestinationAccount());
        transaction.setDescription(transactionDTO.getDescription());
        this.updateCurrentBalance(ACCREDIT, originAccount, transactionDTO.getAmount());
        this.updateCurrentBalance(DEBIT, destinationAccount, transactionDTO.getAmount());
        LOG.debug("Created transaction for destination account: {}", destinationAccount.getAccountNumber());
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
        var transactionToUpdate = new Transaction();
        transactionToUpdate.setId(existingTransaction.get().getId());
        transactionToUpdate.setAmount(transactionDTO.getAmount());
        transactionToUpdate.setTaxCollected(this.calculateTax(transactionDTO.getAmount()));
        transactionToUpdate.setCurrency(transactionDTO.getCurrency());
        transactionToUpdate.setOriginAccount(transactionDTO.getOriginAccount());
        transactionToUpdate.setDestinationAccount(transactionDTO.getDestinationAccount());
        transactionToUpdate.setDescription(transactionDTO.getDescription());

        return new TransactionDTO(transactionRepository.save(transactionToUpdate));
    }

    private void updateCurrentBalance(Operation operation, Account account, Double amount) {
        if (operation.equals(DEBIT)) {
            account.setCurrentBalance(account.getCurrentBalance() + amount);
        } else if (operation.equals(ACCREDIT)) {
            account.setCurrentBalance(account.getCurrentBalance() - (amount + this.calculateTax(amount)));
        }
        accountRepository.save(account);
    }

    private Double calculateTax(Double amount) {
        var tax = 0.0;
        if (amount <= 99) {
            tax = amount * 0.02;
        } else if (amount >= 100) {
            tax = amount * 0.05;
        }
        return tax;
    }
}

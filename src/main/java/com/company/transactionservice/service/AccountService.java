package com.company.transactionservice.service;

import com.company.transactionservice.domain.Account;
import com.company.transactionservice.domain.AccountRepository;
import com.company.transactionservice.repository.TransactionRepository;
import com.company.transactionservice.service.dto.AccountDTO;
import com.company.transactionservice.service.exception.AlreadyExistsException;
import com.company.transactionservice.service.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public Page<AccountDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(account -> {
            account.setTransactions(transactionRepository.findByOriginAccount(account.getAccountNumber()));
            return new AccountDTO(account);
        });
    }

    @Transactional(readOnly = true)
    public AccountDTO getAccount(UUID id) {
        Optional<Account> account = accountRepository.findById(id);
        account.ifPresent(value ->
                value.setTransactions(transactionRepository.findByOriginAccount(value.getAccountNumber())));

        return account.map(AccountDTO::new).orElseThrow(() -> new NotFoundException(String.format("Not found account with id: %s", id)));
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
        var optionalAccount = accountRepository
                .findByAccountNumber(accountDTO.getAccountNumber());

        if (optionalAccount.isPresent()) {
            throw new AlreadyExistsException(String.format("An account with number %s already exists.", optionalAccount.get().getAccountNumber()));
        }

        var account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setCurrentBalance(accountDTO.getCurrentBalance());
        account.setBankAccountName(accountDTO.getBankAccountName());
        LOG.debug("Created transaction for destination account: {}", accountDTO.getAccountNumber());
        return new AccountDTO(accountRepository.save(account));
    }
}

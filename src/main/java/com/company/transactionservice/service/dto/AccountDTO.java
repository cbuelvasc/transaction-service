package com.company.transactionservice.service.dto;

import com.company.transactionservice.domain.Account;
import com.company.transactionservice.domain.Transaction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class AccountDTO {

    private UUID id;

    @NotBlank(message = "The account number must be defined.")
    private String accountNumber;

    @NotNull(message = "The current balance must be defined.")
    @Positive(message = "The current balance must be greater than zero.")
    private Double currentBalance;

    @NotBlank(message = "The bank account name must be defined.")
    private String bankAccountName;

    private List<Transaction> transactions;

    public AccountDTO() {
        super();
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.currentBalance = account.getCurrentBalance();
        this.bankAccountName = account.getBankAccountName();
        this.transactions = account.getTransactions();
    }

    public AccountDTO(String accountNumber, Double currentBalance, String bankAccountName) {
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.bankAccountName = bankAccountName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountDTO)) {
            return false;
        }
        return id != null && id.equals(((AccountDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("accountNumber='" + accountNumber + "'")
                .add("currentBalance=" + currentBalance)
                .add("bankName='" + bankAccountName + "'")
                .add("transactions=" + transactions)
                .toString();
    }
}

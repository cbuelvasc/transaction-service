package com.company.transactionservice.service.dto;

import com.company.transactionservice.domain.Transaction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.StringJoiner;
import java.util.UUID;

public class TransactionDTO {

    private UUID id;

    @NotNull(message = "The amount must be defined.")
    @Positive(message = "The amount must be greater than zero.")
    private Double amount;

    private Double taxCollected;

    @NotBlank(message = "The currency must be defined.")
    private String currency;

    @NotBlank(message = "The origin account must be defined.")
    private String originAccount;

    @NotBlank(message = "The destination account must be defined.")
    private String destinationAccount;

    @NotBlank(message = "The description must be defined.")
    private String description;

    public TransactionDTO() {
        super();
    }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.id();
        this.amount = transaction.amount();
        this.taxCollected = transaction.taxCollected();
        this.currency = transaction.currency();
        this.originAccount = transaction.originAccount();
        this.destinationAccount = transaction.destinationAccount();
        this.description = transaction.description();
    }

    public TransactionDTO(UUID id, Double amount, String currency, String originAccount, String destinationAccount, String description) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTaxCollected() {
        return taxCollected;
    }

    public void setTaxCollected(Double taxCollected) {
        this.taxCollected = taxCollected;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(String originAccount) {
        this.originAccount = originAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }
        return id != null && id.equals(((TransactionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amount=" + amount)
                .add("taxCollected=" + taxCollected)
                .add("currency='" + currency + "'")
                .add("originAccount='" + originAccount + "'")
                .add("destinationAccount='" + destinationAccount + "'")
                .add("description='" + description + "'")
                .toString();
    }
}

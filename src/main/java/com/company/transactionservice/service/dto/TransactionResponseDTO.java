package com.company.transactionservice.service.dto;

import com.company.transactionservice.domain.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;
import java.util.UUID;

public class TransactionResponseDTO {

    private UUID id;

    @JsonProperty("tax_collected")
    private Double taxCollected;

    @JsonProperty("CAD")
    private Double cad;

    public TransactionResponseDTO() {
        super();
    }

    public TransactionResponseDTO(Transaction transaction, Double cad) {
        this.id = transaction.getId();
        this.taxCollected = transaction.getTaxCollected();
        this.cad = cad;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getTaxCollected() {
        return taxCollected;
    }

    public void setTaxCollected(Double taxCollected) {
        this.taxCollected = taxCollected;
    }

    public Double getCad() {
        return cad;
    }

    public void setCad(Double cad) {
        this.cad = cad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionResponseDTO)) {
            return false;
        }
        return id != null && id.equals(((TransactionResponseDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionResponseDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("taxCollected=" + taxCollected)
                .add("cad=" + cad)
                .toString();
    }
}

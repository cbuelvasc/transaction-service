package com.company.transactionservice.domain;

import java.util.UUID;

public record Transaction(

        UUID id,

        Double amount,

        Double taxCollected,

        String currency,

        String originAccount,

        String destinationAccount,

        String description
) {
}

package com.company.transactionservice.web;

import com.company.transactionservice.service.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TransactionJsonTest {

    @Autowired
    private JacksonTester<TransactionDTO> json;

    @Test
    void testSerialize() throws Exception {
        var id = UUID.randomUUID();
        var transaction = new TransactionDTO(id, 5000.0, "USD", "12345601", "12345602", "Pay");

        var jsonContent = json.write(transaction);
        assertThat(jsonContent).extractingJsonPathStringValue("@.id")
                .isEqualTo(transaction.getId().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(transaction.getAmount());
        assertThat(jsonContent).extractingJsonPathStringValue("@.currency")
                .isEqualTo(transaction.getCurrency());
        assertThat(jsonContent).extractingJsonPathStringValue("@.originAccount")
                .isEqualTo(transaction.getOriginAccount());
        assertThat(jsonContent).extractingJsonPathStringValue("@.destinationAccount")
                .isEqualTo(transaction.getDestinationAccount());
        assertThat(jsonContent).extractingJsonPathStringValue("@.description")
                .isEqualTo(transaction.getDescription());
    }

    @Test
    void testDeserialize() throws Exception {
        var id = UUID.randomUUID();
        var content = String.format("""
                {
                     "id": "%s",
                     "amount": 50.0,
                     "taxCollected": null,
                     "currency": "USD",
                     "originAccount": "12345601",
                     "destinationAccount": "12345600",
                     "description": "Test"
                 }
                """, id);
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new TransactionDTO(id, 50.0, "USD", "12345601", "12345600", "Test"));
    }
}

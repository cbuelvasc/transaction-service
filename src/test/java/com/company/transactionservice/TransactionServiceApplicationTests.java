package com.company.transactionservice;

import com.company.transactionservice.service.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenGetRequestWithIdThenTransactionReturned() {
        var id = UUID.randomUUID();
        var transactionToCreate = new TransactionDTO(id, 5000.0, "USD", "12345601", "12345602", "Pay");

        TransactionDTO createdBook = webTestClient
                .post()
                .uri("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionToCreate)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(TransactionDTO.class)
                .value(book -> assertThat(book).isNotNull())
                .returnResult()
                .getResponseBody();

        webTestClient
                .get()
                .uri(String.format("/api/transactions/%s", id))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(TransactionDTO.class)
                .value(transactionDTO -> {
                    assertThat(transactionDTO).isNotNull();
                    assertThat(transactionDTO.getId()).isEqualTo(createdBook.getId());
                });
    }

    @Test
    void whenPostRequestThenTransactionCreated() {
        var expectedTransaction = new TransactionDTO(UUID.fromString("b362ccbc-c742-48c1-a90c-937f5e17bd54"), 50.0, "USD", "12345601", "12345600", "Pay");
        webTestClient.post()
                .uri("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(expectedTransaction)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(TransactionDTO.class)
                .value(transaction -> {
                    assertThat(transaction).isNotNull();
                    assertThat(transaction.getId()).isEqualTo(expectedTransaction.getId());
                });
    }

    @Test
    void whenPutRequestThenTransactionUpdated() {
        var id = UUID.randomUUID();
        var transactionToCreate = new TransactionDTO(id, 5000.0, "USD", "12345601", "12345602", "Pay");

        TransactionDTO createdBook = webTestClient
                .post()
                .uri("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionToCreate)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(TransactionDTO.class)
                .value(book -> assertThat(book).isNotNull())
                .returnResult()
                .getResponseBody();

        var transactionToUpdate =  new TransactionDTO(id, 500.0, "USD", "12345601", "12345600", "Pay");

        webTestClient
                .put()
                .uri(String.format("/api/transactions/%s", id))
                .bodyValue(transactionToUpdate)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionDTO.class).value(transactionDTO -> {
                    assertThat(transactionDTO).isNotNull();
                    assertThat(transactionDTO.getAmount()).isEqualTo(transactionToUpdate.getAmount());
                });
    }

    @Test
    void whenDeleteRequestThenTransactionDeleted() {
        var id = UUID.randomUUID();
        var transactionToCreate = new TransactionDTO(id, 50.0, "USD", "12345601", "12345600", "Pay");
        webTestClient
                .post()
                .uri("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionToCreate)
                .exchange()
                .expectStatus()
                .isCreated();

        webTestClient
                .delete()
                .uri(String.format("/api/transactions/%s", id))
                .exchange()
                .expectStatus()
                .isNoContent();

        webTestClient
                .get()
                .uri(String.format("/api/transactions/%s", id))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .value(errorMessage ->
                        assertThat(errorMessage).isEqualTo(String.format("{\"timestamp\":\"%s\",\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"The transaction with id %s was not found.\",\"stackTrace\":null,\"data\":null}",
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")),
                                id))
                );
    }
}

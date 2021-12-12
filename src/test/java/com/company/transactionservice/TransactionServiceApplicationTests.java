package com.company.transactionservice;

import com.company.transactionservice.service.dto.AccountDTO;
import com.company.transactionservice.service.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class TransactionServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenGetRequestWithIdThenTransactionReturned() {
        this.createAccountToWebClient(new AccountDTO("12345608", 10000.0, "Origin"));
        this.createAccountToWebClient(new AccountDTO("12345603", 15000.0, "Destination"));

        TransactionDTO transactionToCreate = this.createTransactionToWebClient(new TransactionDTO(500.0, "USD", "12345608", "12345603", "Pay"));

        webTestClient
                .get()
                .uri(String.format("/api/transactions/%s", transactionToCreate.getId()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(TransactionDTO.class)
                .value(transactionDTO -> {
                    assertThat(transactionDTO).isNotNull();
                    assertThat(transactionDTO.getId()).isEqualTo(transactionToCreate.getId());
                });
    }

    @Test
    void whenPostRequestThenTransactionCreated() {
        var expectedTransaction = new TransactionDTO(Double.valueOf(50.0).doubleValue(), "USD", "12345601", "12345600", "Pay");
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
                    assertThat(transaction.getId()).isNotNull();
                });
    }

    @Test
    void whenPutRequestThenTransactionUpdated() {
        this.createAccountToWebClient(new AccountDTO("12345604", 10000.0, "Origin"));
        this.createAccountToWebClient(new AccountDTO("12345602", 15000.0, "Destination"));
        TransactionDTO transactionToCreate = this.createTransactionToWebClient(new TransactionDTO(30.0, "USD", "12345604", "12345602", "Pay"));

        var transactionToUpdate = new TransactionDTO(50.0, "USD", "12345604", "12345600", "Pay");

        webTestClient
                .put()
                .uri(String.format("/api/transactions/%s", transactionToCreate.getId()))
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
        TransactionDTO createdTransaction = createTransactionToWebClient(new TransactionDTO(50.0, "USD", "12345601", "12345600", "Pay"));

        webTestClient
                .delete()
                .uri(String.format("/api/transactions/%s", createdTransaction.getId()))
                .exchange()
                .expectStatus()
                .isNoContent();

        webTestClient
                .get()
                .uri(String.format("/api/transactions/%s", createdTransaction.getId()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .value(errorMessage ->
                        assertThat(errorMessage).isEqualTo(String.format("{\"timestamp\":\"%s\",\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"Not found transaction with id: %s\",\"errorCode\":null,\"stackTrace\":null,\"data\":null}",
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")),
                                createdTransaction.getId()))
                );
    }

    private AccountDTO createAccountToWebClient(AccountDTO accountDTO) {
        return webTestClient
                .post()
                .uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountDTO)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(AccountDTO.class)
                .value(dto -> assertThat(dto).isNotNull())
                .returnResult()
                .getResponseBody();
    }

    private TransactionDTO createTransactionToWebClient(TransactionDTO transactionDTO) {
        return webTestClient
                .post()
                .uri("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionDTO)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(TransactionDTO.class)
                .value(dto -> assertThat(dto).isNotNull())
                .returnResult()
                .getResponseBody();
    }
}

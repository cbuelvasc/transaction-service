package com.company.transactionservice.repository;

import com.company.transactionservice.domain.Transaction;
import com.company.transactionservice.service.dto.AccountDTO;
import com.company.transactionservice.service.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
public class TransactionRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(transactionRepository).isNotNull();
    }

    @Test
    void findAllBooks() {
        this.createAccountToWebClient(new AccountDTO("00001", 10000.0, "Origin"));
        this.createAccountToWebClient(new AccountDTO("00002", 15000.0, "Destination"));
        var transactionOne = new Transaction();
        //transactionOne.setId(UUID.randomUUID());
        transactionOne.setAmount(100.0);
        transactionOne.setTaxCollected(1.0);
        transactionOne.setCurrency("USD");
        transactionOne.setOriginAccount("00001");
        transactionOne.setDestinationAccount("00002");
        transactionOne.setDescription("Pay data");
        transactionRepository.save(transactionOne);

        var transactionTwo = new Transaction();
        //transactionTwo.setId(UUID.randomUUID());
        transactionTwo.setAmount(200.0);
        transactionTwo.setTaxCollected(2.0);
        transactionTwo.setCurrency("USD");
        transactionTwo.setOriginAccount("00002");
        transactionTwo.setDestinationAccount("00001");
        transactionTwo.setDescription("Pay data");
        transactionRepository.save(transactionTwo);

        Iterable<Transaction> actualTransactions = transactionRepository.findAll();

        assertThat(StreamSupport.stream(actualTransactions.spliterator(), true)
                .filter(transaction -> transaction.getId().equals(transactionOne.getId()) || transaction.getId().equals(transactionTwo.getId()))
                .collect(Collectors.toList())).hasSize(2);
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
}

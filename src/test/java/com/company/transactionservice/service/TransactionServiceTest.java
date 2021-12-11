package com.company.transactionservice.service;

import com.company.transactionservice.repository.TransactionRepository;
import com.company.transactionservice.service.dto.TransactionDTO;
import com.company.transactionservice.service.exception.AlreadyExistsException;
import com.company.transactionservice.service.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void whenTransactionToCreateAlreadyExistsThenThrows() {
        var id = UUID.randomUUID();
        var transactionToCreate = new TransactionDTO(id, 5000.0, "USD", "12345601", "12345602", "Pay");

        when(transactionRepository.existsById(id)).thenReturn(true);
        assertThatThrownBy(() -> transactionService.createTransaction(transactionToCreate))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage(String.format("A transaction with id %s already exists.", id));
    }

    @Test
    void whenTransactionToDeleteDoesNotExistThenThrows() {
        var id = UUID.randomUUID();
        when(transactionRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> transactionService.removeTransaction(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("The transaction with id %s was not found.", id));
    }
}

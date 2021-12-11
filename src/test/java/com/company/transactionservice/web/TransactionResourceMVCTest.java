package com.company.transactionservice.web;

import com.company.transactionservice.service.TransactionService;
import com.company.transactionservice.service.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionResource.class)
public class TransactionResourceMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void whenGetTransactionNotExistingThenShouldReturn404() throws Exception {
        var id = UUID.randomUUID();
        given(transactionService.viewTransactionDetails(id)).willThrow(NotFoundException.class);
        mockMvc.perform(get(String.format("/api/transactions/%s", id)))
                .andExpect(status().isNotFound());
    }
}

package com.company.transactionservice.web;

import com.company.transactionservice.service.AccountService;
import com.company.transactionservice.service.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountResource.class)
public class AccountResourceMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void whenGetAccountNotExistingThenShouldReturn404() throws Exception {
        var id = UUID.randomUUID();
        given(accountService.getAccount(id)).willThrow(NotFoundException.class);
        mockMvc.perform(get(String.format("/api/accounts/%s", id)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

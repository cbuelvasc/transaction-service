package com.company.transactionservice.web;

import com.company.transactionservice.domain.Transaction;
import com.company.transactionservice.service.TransactionService;
import com.company.transactionservice.service.dto.TransactionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class TransactionResource {

    private final TransactionService transactionService;
    
    public TransactionResource(TransactionService bookService) {
        this.transactionService = bookService;
    }
    
    @GetMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Transaction> getTransaction() {
        return transactionService.viewTransactionList();
    }
    
    @GetMapping(value = "/transactions/{id}")
    public Transaction getTransactionById(@PathVariable UUID id) {
        return transactionService.viewTransactionDetails(id);
    }
    
    @PostMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }
    
    @DeleteMapping(value ="/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable UUID id) {
        transactionService.removeTransaction(id);
    }
    
    @PutMapping(value = "/transactions/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable UUID id, @Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.editTransactionDetails(id, transactionDTO));
    }
}

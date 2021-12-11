package com.company.transactionservice.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String getGreeting() {
        return "Welcome to the book catalog!";
    }
}
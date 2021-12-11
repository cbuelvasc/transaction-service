package com.company.transactionservice.domain;

import com.company.transactionservice.service.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var transactionDTO = new TransactionDTO(UUID.fromString("b362ccbc-c742-48c1-a90c-937f5e17bd54"), 50.0, "USD", "12345601", "12345600", "Pay");
        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnNotDefinedThenValidationFails() {
        var transactionDTO = new TransactionDTO(UUID.fromString("b362ccbc-c742-48c1-a90c-937f5e17bd54"), -50.0, "", "12345601", "12345600", "Pay");
        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The currency must be defined.")
                .contains("The amount must be greater than zero.");
    }
}

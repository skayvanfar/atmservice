package ir.sk.bankservice.controller;

import ir.sk.bankservice.exception.*;
import ir.sk.bankservice.model.operation.OperationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(UserAccountNotFoundException.class)
    public ResponseEntity<OperationResult> handleUserNotFound(UserAccountNotFoundException e) {
        log.error("Account number '{}' was not found in the system for the requested operation.", e.getAccountNumber(), e);
        return buildErrorResult(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<OperationResult> handleInvalidAmount(InvalidAmountException e) {
        log.error("Invalid amount for the requested operation for accountNumber {}.", e.getAccountNumber(), e);
        return buildErrorResult(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<OperationResult> handleInsufficientBalance(InsufficientBalanceException e) {
        log.error("Balance insufficient for the requested operation for account {}.", e.getAccountNumber(), e);
        return buildErrorResult(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<OperationResult> buildErrorResult(UserAccountOperationException e, HttpStatus status) {
        OperationResult operationResult = OperationResult.builder()
                .error(e.getMessage())
                .accountNumber(e.getAccountNumber())
                .build();

        return new ResponseEntity<>(operationResult, status);
    }
}

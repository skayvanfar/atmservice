package ir.sk.bankservice.exception;

import lombok.Getter;

@Getter
public class UserAccountOperationException extends RuntimeException {
    private final String accountNumber;

    public UserAccountOperationException(String accountNumber, String message) {
        super(message);
        this.accountNumber = accountNumber;
    }
}

package ir.sk.atmservice.controller;

import ir.sk.atmservice.model.OperationResult;
import ir.sk.atmservice.model.OperationType;
import ir.sk.atmservice.model.UserAccount;
import ir.sk.atmservice.security.UserAuthenticationService;
import ir.sk.atmservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Basic ATM API with very basic authentication. There are 4 operations supported:
 * 1. authentication (with credentials being accountNumber & pin)
 * 2. see the account's details (account holder name, balance, account number)
 * 3. withdraw an amount from an account
 * 4. deposit an amount into an account
 * See {@link RestControllerExceptionHandler} for exception handling.
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/v1/atm/userAccount")
public class ATMController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private TransactionService transactionService;

    /**
     * Authenticates the user into the ATM system by checking the validity of their credentials.
     */
    @PostMapping("/auth")
    public OperationResult authenticate(@RequestParam String accountNumber, @RequestParam String pin) {
        log.debug("Requesting authentication for account number {}", accountNumber);
        userAuthenticationService.authenticate(accountNumber, pin);
        log.debug("Authentication grated for user account {}", accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.authentication.name())
                .accountNumber(accountNumber)
                .build();
    }

    /**
     * If the user is authenticated, will fetch his account's details and 'deauthenticate' him.
     * Otherwise, he will not be allowed to carry out the operation.
     */
    @GetMapping("/details")
    public OperationResult getAccountDetails(@RequestParam String accountNumber) {
        log.debug("Requesting account details for account number {}", accountNumber);

        verifyUserIsAuthenticated(accountNumber);

        UserAccount account = userAuthenticationService.getUserAccount(accountNumber);
        log.debug("Account details successfully fetched for account number {}, current balance {}, holder name '{}'", accountNumber, account.getBalance(), account.getName());

        deauthenticate(accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.accountDetails.name())
                .accountNumber(accountNumber)
                .currentBalance(account.getBalance())
                .holderName(account.getName())
                .build();
    }

    /**
     * If the user is authenticated, will withdraw the given amount from his account and 'deauthenticate' him.
     * Otherwise, he will not be allowed to carry out the operation.
     */
    @PostMapping("/withdraw")
    public OperationResult withdraw(@RequestParam String accountNumber, @RequestParam Double amount) {
        log.debug("Requesting withdrawal with amount {} for account number {}", amount, accountNumber);

        verifyUserIsAuthenticated(accountNumber);

        transactionService.withdraw(accountNumber, amount);

        deauthenticate(accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.withdraw.name())
                .operatedAmount(amount)
                .currentBalance(null)
                .build();
    }

    /**
     * If the user is authenticated, will deposit the given amount into his account and 'deauthenticate' him.
     * Otherwise, he will not be allowed to carry out the operation.
     */
    @PostMapping("/deposit")
    public OperationResult deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        log.debug("Requesting deposit with amount {} for account number {}", amount, accountNumber);

        verifyUserIsAuthenticated(accountNumber);

        transactionService.deposit(accountNumber, amount);

        deauthenticate(accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.deposit.name())
                .operatedAmount(amount)
                .currentBalance(null)
                .build();
    }

    private void verifyUserIsAuthenticated(String accountNumber) {
        userAuthenticationService.verifyUserIsAuthenticated(accountNumber);
        log.debug("User for the account {} is authenticated, going to carry out his request.", accountNumber);
    }

    private void deauthenticate(String accountNumber) {
        userAuthenticationService.deauthenticate(accountNumber);
        log.debug("User for the account {} was deauthenticated, since he already carried out one operation.", accountNumber);
    }
}

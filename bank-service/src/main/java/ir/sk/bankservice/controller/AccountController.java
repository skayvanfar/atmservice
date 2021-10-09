package ir.sk.bankservice.controller;

import ir.sk.bankservice.service.UserAccountService;
import ir.sk.bankservice.model.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/atm/atmbanks")
@Slf4j
public class AccountController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/{accountNumber}")
    public UserAccount getUserAccounts(@PathVariable("accountNumber") String  accountNumber) {
        log.debug("Requesting account details for account number {}", accountNumber);

        UserAccount account = userAccountService.fetchAccountDetails(accountNumber);
        log.debug("Account details successfully fetched for account number {}, current balance {}, holder name '{}'", accountNumber, account.getBalance(), account.getName());

        return account;
    }
}

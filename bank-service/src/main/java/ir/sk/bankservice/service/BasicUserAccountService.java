package ir.sk.bankservice.service;

import ir.sk.bankservice.exception.InsufficientBalanceException;
import ir.sk.bankservice.exception.InvalidAmountException;
import ir.sk.bankservice.exception.UserAccountNotFoundException;
import ir.sk.bankservice.model.UserAccount;
import ir.sk.bankservice.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BasicUserAccountService implements UserAccountService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public BasicUserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccount fetchAccountDetails(String accountNumber) throws UserAccountNotFoundException {
        return findAccountByNumber(accountNumber);
    }

    @Override
    public UserAccount deposit(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException {
        validateAmount(accountNumber, amount);
        UserAccount userAccount = findAccountByNumber(accountNumber);
        Double newBalance = userAccount.getBalance() + amount;
        updateBalance(userAccount, newBalance);
        return userAccount;
    }

    @Override
    public UserAccount withdraw(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        validateAmount(accountNumber, amount);
        UserAccount userAccount = findAccountByNumber(accountNumber);
        Double currentBalance = userAccount.getBalance();
        if (currentBalance < amount) {
            throw new InsufficientBalanceException(accountNumber, currentBalance, amount);
        }
        Double newBalance = currentBalance - amount;
        updateBalance(userAccount, newBalance);
        return userAccount;
    }

    private UserAccount findAccountByNumber(String accountNumber) throws UserAccountNotFoundException {
        Optional<UserAccount> optional = Optional.ofNullable(userAccountRepository.findByAccountNumber(accountNumber));
        return optional.orElseThrow(() -> new UserAccountNotFoundException(accountNumber));
    }

    private void updateBalance(UserAccount userAccount, Double newBalance) {
        userAccount.setBalance(newBalance);
        userAccountRepository.save(userAccount);
    }

    private void validateAmount(String accountNumber, Double amount) throws InvalidAmountException {
        if (amount == null || amount <= 0) {
            throw new InvalidAmountException(accountNumber, amount);
        }
    }
}

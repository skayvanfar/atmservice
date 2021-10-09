package ir.sk.bankservice.service;

import ir.sk.bankservice.exception.InsufficientBalanceException;
import ir.sk.bankservice.exception.InvalidAmountException;
import ir.sk.bankservice.exception.UserAccountNotFoundException;
import ir.sk.bankservice.model.UserAccount;

public interface UserAccountService {
    /**
     * Fetches the {@link UserAccount} referenced by the given account number.
     * @return the {@link UserAccount}
     * @throws UserAccountNotFoundException if the account number is not found
     */
    UserAccount fetchAccountDetails(String accountNumber) throws UserAccountNotFoundException;
    /**
     * Deposits the given amount in the user account referenced by the given account number.
     *
     * @return the updated {@link UserAccount}
     * @throws UserAccountNotFoundException if the account number is not found
     * @throws InvalidAmountException if the amount is null or <= 0
     */
    UserAccount deposit(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException;
    /**
     * Withdraws the given amount from the user account referenced by the given account number.
     * @return the updated {@link UserAccount}
     * @throws UserAccountNotFoundException if the account number is not found
     * @throws InvalidAmountException if the amount is null or <= 0
     * @throws InsufficientBalanceException if the balance < amount
     */
    UserAccount withdraw(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException, InsufficientBalanceException;
}

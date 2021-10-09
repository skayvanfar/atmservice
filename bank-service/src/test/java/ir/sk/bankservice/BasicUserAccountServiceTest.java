package ir.sk.bankservice;

import ir.sk.bankservice.service.BasicUserAccountService;
import ir.sk.bankservice.exception.InsufficientBalanceException;
import ir.sk.bankservice.exception.InvalidAmountException;
import ir.sk.bankservice.exception.UserAccountNotFoundException;
import ir.sk.bankservice.model.UserAccount;
import ir.sk.bankservice.repository.UserAccountRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BasicUserAccountServiceTest {
    private static final String ACCOUNT_NUMBER = "1234";
    private static final Double INITIAL_BALANCE = 5000D;
    private static final Double VALID_AMOUNT = 1000D;
    private static final Double NEGATIVE_AMOUNT = -1000D;

    @Injectable
    private UserAccountRepository userAccountRepository;

    @Tested
    private BasicUserAccountService userAccountService;

    @Test
    public void testDeposit() {
        UserAccount dbUserAccount = createUserAccount();

        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = dbUserAccount;
        }};

        UserAccount servicedUserAccount = userAccountService.deposit(ACCOUNT_NUMBER, VALID_AMOUNT);

        new Verifications() {{
            UserAccount savedAccount;
            userAccountRepository.save(savedAccount = withCapture());
            times = 1;

            assertEquals(servicedUserAccount, savedAccount);
            assertEquals(dbUserAccount.getId(), savedAccount.getId());
            assertEquals(dbUserAccount.getName(), savedAccount.getName());
            assertEquals(dbUserAccount.getAccountNumber(), savedAccount.getAccountNumber());
            assertEquals(dbUserAccount.getPin(), savedAccount.getPin());
            Double expectedBalance = INITIAL_BALANCE + VALID_AMOUNT;
            assertEquals(expectedBalance, savedAccount.getBalance());

        }};
    }

    @Test(expected = InvalidAmountException.class)
    public void testDepositWhenAmountIsZero() {
        userAccountService.deposit(ACCOUNT_NUMBER, 0D);
    }

    @Test(expected = InvalidAmountException.class)
    public void testDepositWhenAmountIsNegative() {
        userAccountService.deposit(ACCOUNT_NUMBER, NEGATIVE_AMOUNT);
    }

    @Test(expected = InvalidAmountException.class)
    public void testDepositWhenAmountIsNull() {
        userAccountService.deposit(ACCOUNT_NUMBER, null);
    }

    @Test
    public void testWithdraw() {
        UserAccount dbUserAccount = createUserAccount();

        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = dbUserAccount;
        }};

        UserAccount servicedUserAccount = userAccountService.withdraw(ACCOUNT_NUMBER, VALID_AMOUNT);

        new Verifications() {{
            UserAccount savedAccount;
            userAccountRepository.save(savedAccount = withCapture());
            times = 1;

            assertEquals(servicedUserAccount, savedAccount);
            assertEquals(dbUserAccount.getId(), savedAccount.getId());
            assertEquals(dbUserAccount.getName(), savedAccount.getName());
            assertEquals(dbUserAccount.getAccountNumber(), savedAccount.getAccountNumber());
            assertEquals(dbUserAccount.getPin(), savedAccount.getPin());
            Double expectedBalance = INITIAL_BALANCE - VALID_AMOUNT;
            assertEquals(expectedBalance, savedAccount.getBalance());

        }};
    }

    @Test(expected = InvalidAmountException.class)
    public void testWithdrawWhenAmountIsZero() {
        userAccountService.withdraw(ACCOUNT_NUMBER, 0D);
    }

    @Test(expected = InvalidAmountException.class)
    public void testWithdrawWhenAmountIsNegative() {
        userAccountService.withdraw(ACCOUNT_NUMBER, NEGATIVE_AMOUNT);
    }

    @Test(expected = InvalidAmountException.class)
    public void testWithdrawWhenAmountIsNull() {
        userAccountService.withdraw(ACCOUNT_NUMBER, null);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void testWithdrawWhenInsufficientBalance() {
        Double withdrawAmount = INITIAL_BALANCE + 1D;
        userAccountService.withdraw(ACCOUNT_NUMBER, withdrawAmount);
    }

    private UserAccount createUserAccount() {
        UserAccount account = new UserAccount();
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setBalance(INITIAL_BALANCE);
        account.setName("ffff");
        account.setPin("134");
        account.setId("121");
        return account;
    }
}
package ir.sk.atmservice.security;

import ir.sk.atmservice.client.TransactionRestTemplateClient;
import ir.sk.atmservice.exception.UserAccountNotFoundException;
import ir.sk.atmservice.exception.UserAccountPinIncorrectException;
import ir.sk.atmservice.exception.UserAlreadyAuthenticatedException;
import ir.sk.atmservice.exception.UserUnauthenticatedException;
import ir.sk.atmservice.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class BasicUserAuthenticationService implements UserAuthenticationService {
    private final ConcurrentMap<String, Boolean> authenticatedUsers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> attempts = new ConcurrentHashMap<>();

   @Autowired
   TransactionRestTemplateClient transactionRestTemplateClient;

    @Override
    public void authenticate(String accountNumber, String pin) {
        if (authenticatedUsers.containsKey(accountNumber)) {
            throw new UserAlreadyAuthenticatedException(accountNumber);
        }

        UserAccount userAccount = getUserAccount(accountNumber);
        if (userAccount == null) {
            throw new UserAccountNotFoundException(accountNumber);
        }

        if (!userAccount.getPin().equals(pin)) {
            throw new UserAccountPinIncorrectException(accountNumber);
        }
        authenticatedUsers.put(accountNumber, true);
    }

    public UserAccount getUserAccount(String accountNumber) {
        return transactionRestTemplateClient.getAccount(accountNumber);
    }

    @Override
    public void verifyUserIsAuthenticated(String accountNumber) {
        Boolean result = authenticatedUsers.get(accountNumber);
        if (result == null) {
            throw new UserUnauthenticatedException(accountNumber);
        }
    }

    @Override
    public void deauthenticate(String accountNumber) {
        authenticatedUsers.remove(accountNumber);
    }
}

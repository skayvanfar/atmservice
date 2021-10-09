package ir.sk.atmservice.client;

import ir.sk.atmservice.cacherepository.UserAccountRedisRepository;
import ir.sk.atmservice.model.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Rest—Uses an enhanced Spring RestTemplate to invoke the Ribbon-based service
 */
@Component
@Slf4j
public class TransactionRestTemplateClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserAccountRedisRepository userAccountRedisRepo;

    private UserAccount checkRedisCache(String accountNumber) {
        try {
            return userAccountRedisRepo.findUserAccount(accountNumber);
        } catch (Exception ex) {
            log.error("Error encountered while trying to retrieve useraccount {} check Redis Cache.  Exception {}", accountNumber, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(UserAccount userAccount) {
        try {
            userAccountRedisRepo.saveUserAccount(userAccount);
        } catch (Exception ex) {
            log.error("Unable to cache userAccount {} in Redis. Exception {}", userAccount.getId(), ex);
        }
    }

    public UserAccount getAccount(String accountNumber) {

        String serviceUri = String.format("http://atmbankservice/v1/atm/atmbanks/%s", accountNumber);

        UserAccount userAccount = checkRedisCache(accountNumber);

        if (userAccount != null) {
            log.debug("I have successfully retrieved an userAccount {} from the redis cache: {}", accountNumber, userAccount);
            return userAccount;
        }

        log.debug("Unable to locate userAccount from the redis cache: {}.", accountNumber);

        ResponseEntity<UserAccount> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, UserAccount.class, accountNumber);

        userAccount = restExchange.getBody();
        if (userAccount != null) {
            cacheOrganizationObject(userAccount);
        }
        return userAccount;
    }
}

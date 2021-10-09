package ir.sk.atmservice.cacherepository;

import ir.sk.atmservice.model.UserAccount;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 3/26/2020.
 */
public interface UserAccountRedisRepository {
    void saveUserAccount(UserAccount userAccount);
    void updateUserAccount(UserAccount userAccount);
    void deleteUserAccount(String accountNumber);
    UserAccount findUserAccount(String accountNumber);
}

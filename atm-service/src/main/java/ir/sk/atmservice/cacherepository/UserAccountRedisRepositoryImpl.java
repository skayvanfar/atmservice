package ir.sk.atmservice.cacherepository;

import ir.sk.atmservice.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 3/26/2020.
 */
@Repository
public class UserAccountRedisRepositoryImpl implements UserAccountRedisRepository {

    // The name of the hash in your Redis server where organization data is stored
    private static final String HASH_NAME ="useraccount";

    private RedisTemplate<String, UserAccount> redisTemplate;

    // The HashOperations class is a set of data is stored Spring helper methods for carrying out data operations on the Redis server
    private HashOperations hashOperations;

    public UserAccountRedisRepositoryImpl(){
        super();
    }

    @Autowired
    private UserAccountRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void saveUserAccount(UserAccount userAccount) {
        hashOperations.put(HASH_NAME, userAccount.getId(), userAccount);
    }

    @Override
    public void updateUserAccount(UserAccount userAccount) {
        hashOperations.put(HASH_NAME, userAccount.getId(), userAccount);
    }

    @Override
    public void deleteUserAccount(String accountNumber) {
        hashOperations.delete(HASH_NAME, accountNumber);
    }

    @Override
    public UserAccount findUserAccount(String accountNumber) {
        return (UserAccount) hashOperations.get(HASH_NAME, accountNumber);
    }
}

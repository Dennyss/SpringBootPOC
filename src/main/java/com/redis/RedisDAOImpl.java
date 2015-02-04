package com.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * Created by Denys Kovalenko on 9/23/2014.
 */
public class RedisDAOImpl implements RedisDAO, InitializingBean {
    public static final String DELIMITER = "#";
    private StringRedisTemplate stringRedisTemplate;

    private JedisConnectionFactory jedisConnectionFactory;

    public RedisDAOImpl(JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }


    public void save(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void saveAll(List<String> entries) {
        for (String entry : entries) {
            String[] parts = entry.split(DELIMITER);
            String key = parts[0];
            String value = parts[1];

            save(key, value);
        }
    }

    public String retrieve(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(final String key) {
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.del(key.getBytes());
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        stringRedisTemplate = new StringRedisTemplate(jedisConnectionFactory);
        stringRedisTemplate.afterPropertiesSet();
    }
}

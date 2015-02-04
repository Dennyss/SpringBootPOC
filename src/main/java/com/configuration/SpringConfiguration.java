package com.configuration;

import com.jms.JMSMessageConsumer;
import com.jms.JMSMessageProducer;
import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.spring.context.config.EnableReactor;
import redis.clients.jedis.JedisShardInfo;

/**
 * Created by Denys Kovalenko on 10/6/2014.
 */
@Configuration
@EnableReactor
@EnableAutoConfiguration
public class SpringConfiguration {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    @Bean
    Reactor createReactor(Environment env) {
        return Reactors.reactor()
                .env(env)
                .dispatcher(Environment.RING_BUFFER)
                .get();
    }

    @Bean
    @Scope("singleton")
    public JedisConnectionFactory getJedisConnectionFactory(){
        return new JedisConnectionFactory(new JedisShardInfo(host, port));
    }

    @Bean
    @Scope("singleton")
    public RedisDAOImpl getRedisDAO(){
        return new RedisDAOImpl(getJedisConnectionFactory());
    }

    @Bean(name = "jmsMessageConsumer")
    public JMSMessageConsumer createJMSMessageConsumer(){
        return new JMSMessageConsumer();
    }

    @Bean
    @DependsOn("jmsMessageConsumer")
    public JMSMessageProducer createJMSMessageProducer(){
        return new JMSMessageProducer();
    }

}

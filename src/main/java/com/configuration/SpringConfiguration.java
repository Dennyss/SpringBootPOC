package com.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Denys Kovalenko on 10/6/2014.
 */
@Component
@ConfigurationProperties(prefix="connection")
public class SpringConfiguration implements InitializingBean {
    private static final Logger LOGGER = LogManager.getLogger(SpringConfiguration.class);

    //@Value("${connection-redisHost}")
    private String redisHost;


    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("The property value host is: " + redisHost);
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }
}

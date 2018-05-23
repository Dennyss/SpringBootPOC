package com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

/**
 * Created by Denys Kovalenko on 5/23/2018.
 */
@SpringBootApplication
public class Application {
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        LOGGER.info("Application stated successfully!");
    }
}
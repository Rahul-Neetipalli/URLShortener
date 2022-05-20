package com.shorten.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.shorten"})
public class Application {
    private static Logger LOGGER = LogManager.getLogger(Application.class);
    public static void main(String[] args) {
	System.out.println(System.getProperty("java.io.tmpdir"));
	LOGGER.info("Starting Application ..");
	SpringApplication.run(Application.class, args);
	LOGGER.info("Started Succesfully ..");

    }

}

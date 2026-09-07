package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@SpringBootApplication
@ConfigurationPropertiesScan
public class StudentManagementSystemApplication {

    private static final Logger log =
            LoggerFactory.getLogger(
                    StudentManagementSystemApplication.class
            );

    public static void main(String[] args) {

        SpringApplication.run(
                StudentManagementSystemApplication.class,
                args
        );

        log.info("================================================");
        log.info("   Student Management System Started Successfully");
        log.info("   Version : V2");
        log.info("   Status  : RUNNING");
        log.info("================================================");
    }
}
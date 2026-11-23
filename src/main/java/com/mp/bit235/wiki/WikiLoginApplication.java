/*
 * BIT235 Assessment 2 - Part A: Wiki Administrator Login
 * Student: Xinle Xie - S1580929
 *
 * WikiLoginApplication is the entry point of the Spring Boot application.
 */
package com.mp.bit235.wiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Enables component scanning, auto-configuration and property support.
public class WikiLoginApplication {

    public static void main(String[] args) {
        // Boots the embedded Tomcat server and starts the Spring context.
        SpringApplication.run(WikiLoginApplication.class, args);
    }
}

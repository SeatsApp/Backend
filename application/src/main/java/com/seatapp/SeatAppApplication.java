package com.seatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Main SeatAppApplication class.
 */
@SpringBootApplication
public class SeatAppApplication {
    /**
     * Main method.
     *
     * @param args the arguments given with the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(SeatAppApplication.class, args);
    }

    /**
     * Set the time zone of the server.
     */
    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    }
}

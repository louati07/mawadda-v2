package com.mawadda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MawaddaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MawaddaApplication.class, args);
    }
}

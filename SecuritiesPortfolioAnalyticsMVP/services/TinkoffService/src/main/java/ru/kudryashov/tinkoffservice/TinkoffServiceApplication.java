package ru.kudryashov.tinkoffservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TinkoffServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffServiceApplication.class, args);
    }

}

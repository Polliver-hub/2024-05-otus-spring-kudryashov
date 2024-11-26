package ru.kudryashov.userportfoliosservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserPortfoliosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserPortfoliosServiceApplication.class, args);
    }

}

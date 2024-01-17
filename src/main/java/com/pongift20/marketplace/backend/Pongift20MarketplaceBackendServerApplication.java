package com.pongift20.marketplace.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class Pongift20MarketplaceBackendServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Pongift20MarketplaceBackendServerApplication.class, args);
    }

}

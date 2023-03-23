package com.example.bookeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BookeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookeServiceApplication.class, args);
    }

}

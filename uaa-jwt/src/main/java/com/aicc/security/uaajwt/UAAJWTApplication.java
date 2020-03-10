package com.aicc.security.uaajwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UAAJWTApplication {
    public static void main(String[] args) {
        SpringApplication.run(UAAJWTApplication.class, args);
    }
}

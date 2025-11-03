package com.example.commerce_back_office;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//develop 을 위한
@EnableJpaAuditing
@SpringBootApplication
public class CommerceBackOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceBackOfficeApplication.class, args);
    }

}

package com.sparta.spartaeats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpartaEatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpartaEatsApplication.class, args);
    }

}

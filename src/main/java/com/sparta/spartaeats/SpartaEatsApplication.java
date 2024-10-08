package com.sparta.spartaeats;

import com.sparta.spartaeats.auditing.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.sparta.spartaeats")
@EnableAspectJAutoProxy
@EnableJpaAuditing
public class SpartaEatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpartaEatsApplication.class, args);
    }

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }
}

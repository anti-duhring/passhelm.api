package com.passhelm.passhelm.config;

import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.repository.PasswordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PasswordConfig {

    @Bean
    CommandLineRunner commandLineRunner3(PasswordRepository repository) {
        return args -> {
            Password password1 = new Password(
                    1L,
                    1L,
                    "Instagram",
                    "mtvozzy",
                    "123456"
            );
            Password password2 = new Password(
                    1L,
                    2L,
                    "Facebook",
                    "mateusvnlima",
                    "123456"
            );

            repository.saveAll(List.of(password1, password2));
        };
    }
}

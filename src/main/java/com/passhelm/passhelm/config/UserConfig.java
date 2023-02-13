package com.passhelm.passhelm.config;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User mateus = new User(
                    "mateusvnlima",
                    "Mateus Vinicius",
                    "mateusvnlima@gmail.com",
                    new BCryptPasswordEncoder().encode("123456"),
                    List.of("ROLE_USER", "ROLE_ADMIN")
            );
            User tom = new User(
                    "tombrady",
                    "Tom Brady",
                    "tombrady@gmail.com",
                    new BCryptPasswordEncoder().encode("123456"),
                    List.of("ROLE_USER")
            );

            repository.saveAll(
                    List.of(mateus, tom)
            );
        };
    }
}

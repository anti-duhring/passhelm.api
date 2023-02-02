package com.passhelm.passhelm.config;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CategoryConfig {

    @Bean
    CommandLineRunner commandLineRunner2(CategoryRepository repository) {
        return args -> {
            Category category1 = new Category(
                    1L,
                    "Food",
                    "#000000"
                    );

            Category category2 = new Category(
                    2L,
                    "Drink",
                    "#000000"
                    );

            repository.saveAll(
                    List.of(category1, category2)
            );
        };
    }
}

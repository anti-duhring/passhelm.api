package com.passhelm.passhelm.repository;

import com.passhelm.passhelm.models.Category;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindAllCategoriesByUserId() {
        Long userId = 1L;

        Category newCategory = new Category(
                userId,
                "Test Category",
                "#000000"
        );

        em.persist(newCategory);

        List<Category> allCategories = categoryRepository.findAllByUserId(userId);

        assertEquals(1, allCategories.size());
    }
}
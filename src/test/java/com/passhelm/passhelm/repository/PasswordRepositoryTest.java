package com.passhelm.passhelm.repository;

import com.passhelm.passhelm.models.Password;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PasswordRepositoryTest {

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private TestEntityManager em;
    @Test
    void shouldFindById() {
        Long userId = 1L;
        Long categoryId = 1L;

        Password newPassword = new Password(
                userId,
                categoryId,
                "password",
                "password",
                "password"
        );

        em.persist(newPassword);

        Optional<Password> foundPassword = passwordRepository.findById(newPassword.getId());

        Assertions.assertNotNull(foundPassword);
        Assertions.assertEquals(foundPassword.get().getId(), newPassword.getId());

    }

    @Test
    void shouldFindAllByUserId() {
        Long userId = 1L;
        Long categoryId = 1L;

        Password newPassword = new Password(
                userId,
                categoryId,
                "password",
                "password",
                "password"
        );

        em.persist(newPassword);

        List<Password> foundPasswords = passwordRepository.findAllByUserId(userId);

        Assertions.assertTrue(foundPasswords.size() > 0);

    }
}
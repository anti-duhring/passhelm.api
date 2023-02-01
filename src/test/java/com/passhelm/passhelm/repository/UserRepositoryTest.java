package com.passhelm.passhelm.repository;

import com.passhelm.passhelm.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void shouldFindAUserByUsername() {

        String username = "mateusvnlima";
        Optional<User> user = repository.findByUsername(username);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(username, user.get().getUsername());
    }

    @Test
    public void shouldFindAUserByEmail() {

        String email = "tombrady@gmail.com";
        Optional<User> user = repository.findByEmail(email);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(email, user.get().getEmail());
    }
}
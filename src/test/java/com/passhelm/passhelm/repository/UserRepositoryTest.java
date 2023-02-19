package com.passhelm.passhelm.repository;

import com.passhelm.passhelm.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void shouldFindAUserByUsername() {

        User newUser = new User(
                "mateusvnlima",
                "Mateus Vinicius",
                "mateusvnlima@gmail.com",
                "123456",
                List.of("ROLE_USER", "ROLE_ADMIN")
        );
        em.persist(newUser);

        String username = "mateusvnlima";
        Optional<User> user = repository.findByUsername(username);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(username, user.get().getUsername());
    }

    @Test
    public void shouldNotFindAUserByUsername() {

        String username = "mateusvnlima";
        Optional<User> user = repository.findByUsername(username);

        Assertions.assertEquals(Optional.empty(), user);
    }

    @Test
    public void shouldFindAUserByEmail() {

        User newUser = new User(
                "mateusvnlima",
                "Mateus Vinicius",
                "mateusvnlima@gmail.com",
                "123456",
                List.of("ROLE_USER", "ROLE_ADMIN")
        );
        em.persist(newUser);

        String email = "mateusvnlima@gmail.com";
        Optional<User> user = repository.findByEmail(email);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(email, user.get().getEmail());
    }

    @Test
    public void shouldNotFindAUserByEmail() {
        String email = "mateusvnlima@gmail.com";
        Optional<User> user = repository.findByEmail(email);

        Assertions.assertEquals(Optional.empty(), user);
    }
}
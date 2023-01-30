package com.passhelm.passhelm.repository;

import com.passhelm.passhelm.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

/*    @Query("SELECT u FROM User u WHERE u.username =?1")*/
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

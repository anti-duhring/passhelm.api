package com.passhelm.passhelm.repository;

import com.passhelm.passhelm.models.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    Optional<Password> findById(Long id);
    List<Password> findAllByUserId(Long userId);
}

package com.passhelm.passhelm.repository;

import com.passhelm.passhelm.models.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findAllByUserId(Long userId);
}

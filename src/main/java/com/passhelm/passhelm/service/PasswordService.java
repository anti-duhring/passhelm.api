package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {
    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public List<Password> getAllPasswordsByUserId(Long userId) {
        return passwordRepository.findAllByUserId(userId);
    }
}

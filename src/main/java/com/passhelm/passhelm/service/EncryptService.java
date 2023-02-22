package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Password;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EncryptService {

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor encryptor;
    public Password encryptPassword(Password password) {
        password.setLogin(encryptor.encrypt(password.getLogin()));
        password.setPassword(encryptor.encrypt(password.getPassword()));

        return password;
    }

    public Password decryptPassword(Password password) {
        password.setLogin(encryptor.decrypt(password.getLogin()));
        password.setPassword(encryptor.decrypt(password.getPassword()));

        return password;
    }
}

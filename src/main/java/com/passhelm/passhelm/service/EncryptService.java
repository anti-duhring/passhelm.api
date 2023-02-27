package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Password;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
public class EncryptService {

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor encryptor;
    public Password encryptPassword(Password password) {
        Password encryptedPassword = new Password();
        BeanUtils.copyProperties(password, encryptedPassword);

        encryptedPassword.setLogin(encryptor.encrypt(password.getLogin()));
        encryptedPassword.setPassword(encryptor.encrypt(password.getPassword()));

        return encryptedPassword;
    }

    public Password decryptPassword(Password password) {
        Password decryptedPassword = new Password();
        BeanUtils.copyProperties(password, decryptedPassword);

        decryptedPassword.setLogin(encryptor.decrypt(password.getLogin()));
        decryptedPassword.setPassword(encryptor.decrypt(password.getPassword()));

        return decryptedPassword;
    }

    public String encryptProperty(String text) {
        return encryptor.encrypt(text);
    }
}

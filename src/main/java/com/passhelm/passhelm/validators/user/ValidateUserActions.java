package com.passhelm.passhelm.validators.user;

import com.passhelm.passhelm.models.User;

import java.security.Principal;

public interface ValidateUserActions {

    void validate(Principal principal) throws Exception;

    void validate(Principal principal, Long userId) throws Exception;

    void validate(User user) throws Exception;

    void validate(User user, String password) throws Exception;

}

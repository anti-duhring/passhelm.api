package com.passhelm.passhelm.validators.user;

import java.security.Principal;

public interface ValidateUserActions {

    void validate(Principal principal) throws Exception;

    void validate(Principal principal, Long userId) throws Exception;

}

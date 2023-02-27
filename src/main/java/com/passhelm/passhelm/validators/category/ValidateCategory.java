package com.passhelm.passhelm.validators.category;

import com.passhelm.passhelm.models.Category;

import java.nio.file.AccessDeniedException;

public interface ValidateCategory {

    void validate(Category category) throws Exception;

    void validate(Category category, Long userId) throws Exception;
}

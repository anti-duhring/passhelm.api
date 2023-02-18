package com.passhelm.passhelm.validators.category;

import com.passhelm.passhelm.models.Category;

public interface ValidateCategory {

    void validate(Category category);

    void validate(Category category, Long userId);
}

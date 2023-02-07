package com.passhelm.passhelm.records;

import com.passhelm.passhelm.models.Category;

public record CategoryResponse(Long id, Long userId, String label, String color) {

    public CategoryResponse(Category category) {
        this(category.getId(), category.getUserId(), category.getLabel(), category.getColor());
    }
}

package com.example.demo.service;

import com.example.demo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getCategories();

    void saveCategory(Category category);

    Optional<Category> categoryById(Long id);

    void deleteCategory(Long id);
}

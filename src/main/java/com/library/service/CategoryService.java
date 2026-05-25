package com.library.service;

import com.library.dto.CategoryRequest;
import com.library.model.Category;
import com.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category addCategory(CategoryRequest request) {
        validateCategoryRequest(request);

        if (categoryRepository.existsByName(request.getName().trim())) {
            throw new RuntimeException("Category already exists: " + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryRequest request) {
        validateCategoryRequest(request);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (categoryRepository.existsByName(request.getName().trim()) &&
                !category.getName().equalsIgnoreCase(request.getName().trim())) {
            throw new RuntimeException("Category already exists: " + request.getName());
        }

        category.setName(request.getName().trim());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private void validateCategoryRequest(CategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Category name is required!");
        }
        if (request.getName().trim().length() > 100) {
            throw new RuntimeException("Category name must not exceed 100 characters!");
        }
    }
}
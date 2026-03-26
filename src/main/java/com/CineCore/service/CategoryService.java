package com.CineCore.service;

import com.CineCore.entity.Category;
import com.CineCore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    public Optional<Category> updateCategory(Long id, Category category) {
        Optional<Category> categoryFound = categoryRepository.findById(id);
        if (categoryFound.isPresent()) {
            Category categoryUpdated = categoryFound.get();
            categoryUpdated.setName(category.getName());

            categoryRepository.save(categoryUpdated);
            return Optional.of(categoryUpdated);
        }
        return Optional.empty();
    }

    public void deleteCategoryById(Long id){
        categoryRepository.deleteById(id);
    }
}

package com.CineCore.controller;

import com.CineCore.entity.Category;
import com.CineCore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cinecore/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public Category saveCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryFounded = categoryService.getCategoryById(id);
        return categoryFounded.orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteCategoryById(@PathVariable Long id){
        if(getCategoryById(id) != null){
            categoryService.deleteCategoryById(id);
            return "Categoria de id " + id + " deletada com sucesso.";
        }
        return "Não foi possível encontrar uma categoria com o id " + id + ".";
    }

}

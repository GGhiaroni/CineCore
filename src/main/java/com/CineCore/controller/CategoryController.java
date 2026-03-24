package com.CineCore.controller;

import com.CineCore.entity.Category;
import com.CineCore.mapper.CategoryMapper;
import com.CineCore.request.CategoryRequest;
import com.CineCore.response.CategoryResponse;
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
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return categories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .toList();
    }

    @PostMapping
    public CategoryResponse saveCategory(@RequestBody CategoryRequest category) {
        Category categoryMapped = CategoryMapper.toCategory(category);
        Category categorySaved = categoryService.saveCategory(categoryMapped);
        return CategoryMapper.toCategoryResponse(categorySaved);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryFounded = categoryService.getCategoryById(id);
        if (categoryFounded.isPresent()){
            return CategoryMapper.toCategoryResponse(categoryFounded.get());
        }
        return null;
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

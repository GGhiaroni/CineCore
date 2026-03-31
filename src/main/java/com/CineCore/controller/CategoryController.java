package com.CineCore.controller;

import com.CineCore.entity.Category;
import com.CineCore.mapper.CategoryMapper;
import com.CineCore.request.CategoryRequest;
import com.CineCore.response.CategoryResponse;
import com.CineCore.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cinecore/category")
@RequiredArgsConstructor
@Tag(name="Category", description = "Recurso responsável pelo gerenciamento das categorias.")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if(categories != null) {
            List<CategoryResponse> categoriesResponseList = categories.stream()
                    .map(CategoryMapper::toCategoryResponse)
                    .toList();
            return ResponseEntity.ok(categoriesResponseList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma categoria foi encontrada.");
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest category) {
        Category categoryMapped = CategoryMapper.toCategory(category);
        Category categorySaved = categoryService.saveCategory(categoryMapped);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CategoryMapper.toCategoryResponse(categorySaved)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryFound = categoryService.getCategoryById(id);
        if (categoryFound.isPresent()) {
            return ResponseEntity.ok(CategoryMapper.toCategoryResponse(categoryFound.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível "
        + "localizar uma categoria de id " + id + ".");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest){
        return categoryService.updateCategory(id, CategoryMapper.toCategory(categoryRequest))
                .map(category -> ResponseEntity.ok(CategoryMapper.toCategoryResponse(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível " +
                "encontrar uma categoria com o id " + id + ".");
    }

}

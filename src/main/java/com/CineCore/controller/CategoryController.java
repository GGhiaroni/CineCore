package com.CineCore.controller;

import com.CineCore.entity.Category;
import com.CineCore.mapper.CategoryMapper;
import com.CineCore.request.CategoryRequest;
import com.CineCore.response.CategoryResponse;
import com.CineCore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Lista todas as categorias cadastradas", description = "Rota lista todas as categorias cadastradas.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categorias não encontradas."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
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

    @Operation(summary = "Cria uma nova categoria", description = "Rota cria uma nova categoria no banco de dados.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na criação da categoria."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest category) {
        Category categoryMapped = CategoryMapper.toCategory(category);
        Category categorySaved = categoryService.saveCategory(categoryMapped);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CategoryMapper.toCategoryResponse(categorySaved)
        );
    }

    @Operation(summary = "Lista a categoria por ID", description = "Rota lista a categoria pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryFound = categoryService.getCategoryById(id);
        if (categoryFound.isPresent()) {
            return ResponseEntity.ok(CategoryMapper.toCategoryResponse(categoryFound.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível "
        + "localizar uma categoria de id " + id + ".");
    }

    @Operation(summary = "Altera a categoria por ID.", description = "Rota altera a categoria pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Categoria alterada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest){
        return categoryService.updateCategory(id, CategoryMapper.toCategory(categoryRequest))
                .map(category -> ResponseEntity.ok(CategoryMapper.toCategoryResponse(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta a categoria por ID.", description = "Rota deleta a categoria pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Categoria deletada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
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

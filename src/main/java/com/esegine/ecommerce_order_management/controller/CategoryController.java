package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.Category;
import com.esegine.ecommerce_order_management.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@Valid @RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public List<Category> getALLCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/api/categories/{id}")
    public Category updateCategory(@PathVariable Long id, @Valid @RequestBody
    Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id ) {
        categoryService.deleteCategory(id);
    }

}

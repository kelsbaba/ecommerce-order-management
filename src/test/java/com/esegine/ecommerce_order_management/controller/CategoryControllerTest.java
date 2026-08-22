package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.Category;
import com.esegine.ecommerce_order_management.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void createCategory_shouldReturnCreatedCategory() {
        Category category = new Category();

        category.setName("Electronics");

        when(categoryService.createCategory(category)).thenReturn(category);

        Category result = categoryController.createCategory(category);

        assertEquals(category, result);

        verify(categoryService).createCategory(category);
    }

    @Test
    void getCategoryById_shouldReturnCategory() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        Category result = categoryController.getCategoryById(categoryId);

        assertEquals(category, result);

        verify(categoryService).getCategoryById(categoryId);
    }

    @Test
    void getCategoryById_shouldThrowException_WhenCategoryDoesNotExist() {
        Long categoryId = 99L;

        when(categoryService.getCategoryById(categoryId)).thenThrow(new
                RuntimeException("Category not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoryController.getCategoryById(categoryId));
        assertEquals("Category not found", exception.getMessage());

        verify(categoryService).getCategoryById(categoryId);
    }

    @Test
    void getAllCategories_shouldReturnAllCategories() {
        Category category1 = new Category("Electronics");
        Category category2 = new Category("Clothing");

        List<Category> categories = List.of(category1, category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        List<Category> result = categoryController.getALLCategories();

        assertEquals(categories, result);

        verify(categoryService).getAllCategories();
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setName("Updated electronics");

        when(categoryService.updateCategory(categoryId, category)).thenReturn(category);

        Category result = categoryController.updateCategory(categoryId, category);
        assertEquals(category, result);

        verify(categoryService).updateCategory(categoryId, category);
    }

    @Test
    void updateCategory_shouldThrowException_WhenCategoryDoesNotExist() {
        Long categoryId = 99L;

        Category category = new Category();
        category.setName("Updated Electronics");

        when(categoryService.updateCategory(categoryId, category)).thenThrow(
                new RuntimeException("Category not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoryController.updateCategory(categoryId,category));

        assertEquals("Category not found", exception.getMessage());
        verify(categoryService).updateCategory(categoryId,category);

    }

    @Test
    void deleteCategory_shouldDeleteCategory() {
        Long categoryId = 1L;

        doNothing().when(categoryService).deleteCategory(categoryId);

        categoryController.deleteCategory(categoryId);

        verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    void deleteCategory_shouldThrowException_WhenCategoryDoesNotExist() {
        Long categoryId = 99L;

        doThrow(new RuntimeException("Category not found")).when(categoryService)
                .deleteCategory(categoryId);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoryController.deleteCategory(categoryId));

        assertEquals("Category not found", exception.getMessage());

        verify(categoryService).deleteCategory(categoryId);
    }
}

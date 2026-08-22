package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Category;
import com.esegine.ecommerce_order_management.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
   private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_shouldSaveCategory() {
        Category category = new Category("Electronics");

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void getCategoryById_shouldReturnCategory() {
        Long categoryId = 1L;
        Category category = new Category("Electronics");

        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(categoryId);

        assertEquals(category, result);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void getCategoryById_shouldThrowException_WhenCategoryDoesNotExist() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.getCategoryById(categoryId)
        );

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getAllCategories_shouldReturnAllCategories() {
        Category category1 = new Category("Electronics");

        Category category2 = new Category("Clothing");

        List<Category> categories = List.of(category1, category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(categories, result);
        verify(categoryRepository).findAll();
    }

    @Test
    void updateCategory_shouldUpdateCategory() {
        Long categoryId = 1L;
        Category existingCategory = new Category("Electronics");
        existingCategory.setId(categoryId);

        Category updatedCategory = new Category("Computers");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(categoryId, updatedCategory);

        assertEquals("Computers", result.getName());
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(existingCategory);
    }

    @Test
    void updateCategory_shouldThrowException_WhenCategoryDoesNotExist() {
        Long categoryId = 99L;
        Category updatedCategory = new Category("Computers");

        when(categoryRepository.findById(categoryId )).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.updateCategory(categoryId , updatedCategory)
        );

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(categoryId );
    }

    @Test
    void deleteCategory_shouldDeleteCategory() {
        Long categoryId = 1L;
        Category category = new Category("Electronics");
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_shouldThrowException_WhenCategoryDoesNotExist() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.deleteCategory(categoryId)
        );

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(categoryId);
    }
}

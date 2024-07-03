package com.sonata.portfoliomanagement.JunitTest;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sonata.portfoliomanagement.controllers.MD_CategoryController;
import com.sonata.portfoliomanagement.interfaces.MD_CategoryRepository;
import com.sonata.portfoliomanagement.model.MD_Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MdCategoryTest {





    @InjectMocks
    private MD_CategoryController categoryController;

    @Mock
    private MD_CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllData() {
        // Mock data
        MD_Category category1 = new MD_Category(1, "Category1");
        MD_Category category2 = new MD_Category(2, "Category2");
        List<MD_Category> mockCategories = Arrays.asList(category1, category2);

        // Mock repository method call
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        // Call the controller method
        ResponseEntity<List<MD_Category>> response = categoryController.getAllData();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size()); // Assuming you expect 2 categories in the response
        assertEquals("Category1", response.getBody().get(0).getCategory());
        assertEquals("Category2", response.getBody().get(1).getCategory());
    }
    @Test
    public void testCreateMdCategory_Success() {
        // Mock data
        MD_Category newCategory = new MD_Category(1, "NewCategory");

        // Mock repository behavior
        when(categoryRepository.findByCategory(newCategory.getCategory())).thenReturn(Optional.empty());
        when(categoryRepository.save(newCategory)).thenReturn(newCategory);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = categoryController.createMdCategory(newCategory);

        // Assertions for success case
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue(responseBody.containsKey("message"));
        assertTrue(responseBody.containsKey("createdCategory"));
        assertEquals("Category 'NewCategory' saved successfully.", responseBody.get("message"));
        assertEquals(newCategory, responseBody.get("createdCategory"));
    }

    @Test
    public void testCreateMdCategory_Conflict() {
        // Mock data
        MD_Category existingCategory = new MD_Category(1, "ExistingCategory");
        MD_Category newCategory = new MD_Category(2, "ExistingCategory");

        // Mock repository behavior
        when(categoryRepository.findByCategory(newCategory.getCategory())).thenReturn(Optional.of(existingCategory));

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = categoryController.createMdCategory(newCategory);

        // Assertions for conflict case
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue(responseBody.containsKey("message"));
        assertEquals("Duplicate entry: Category 'ExistingCategory' already exists.", responseBody.get("message"));
    }

    @Test
    public void testUpdateMdCategory_Success() {
        // Mock data
        MD_Category existingCategory = new MD_Category(1, "ExistingCategory");
        MD_Category updatedCategory = new MD_Category(1, "UpdatedCategory");

        // Mock repository behavior
        when(categoryRepository.findById(existingCategory.getId())).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findByCategory(updatedCategory.getCategory())).thenReturn(Optional.empty());
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = categoryController.updateMdCategory(updatedCategory);

        // Assertions for success case
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue(responseBody.containsKey("message"));
        assertTrue(responseBody.containsKey("updatedCategory"));
        assertEquals("Category name updated from 'ExistingCategory' to 'UpdatedCategory'. ", responseBody.get("message"));
        assertEquals(updatedCategory, responseBody.get("updatedCategory"));
    }



    @Test
    public void testUpdateMdCategory_NotFound() {
        // Mock data
        MD_Category updatedCategory = new MD_Category(1, "UpdatedCategory");

        // Mock repository behavior (category not found)
        when(categoryRepository.findById(updatedCategory.getId())).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = categoryController.updateMdCategory(updatedCategory);

        // Assertions for not found case
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteCategoriesByIds_Success() {
        // Mock data
        List<Integer> idsToDelete = List.of(1, 2);
        MD_Category category1 = new MD_Category(1, "Category1");
        MD_Category category2 = new MD_Category(2, "Category2");

        // Mock repository behavior
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(category2));

        // Call the controller method
        ResponseEntity<String> response = categoryController.deleteCategoriesByIds(idsToDelete);

        // Assertions for success case
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert the response message
        String expectedMessage = "Categories: [Category1, Category2] deleted successfully";
        assertEquals(expectedMessage, response.getBody().trim());

        // Verify that deleteById was called for each ID
        verify(categoryRepository).deleteById(1);
        verify(categoryRepository).deleteById(2);
    }

    @Test
    public void testDeleteCategoriesByIds_NotFound() {
        // Mock data
        List<Integer> idsToDelete = List.of(1, 2);

        // Mock repository behavior (categories not found)
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<String> response = categoryController.deleteCategoriesByIds(idsToDelete);

        // Assertions for not found case
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No categories found with IDs: [1, 2]", response.getBody());
    }

    @Test
    public void testDeleteCategoriesByIds_Mixed() {
        // Mock data
        List<Integer> idsToDelete = List.of(1, 2, 3);
        MD_Category category1 = new MD_Category(1, "Category1");

        // Mock repository behavior (one category not found)
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());
        when(categoryRepository.findById(3)).thenReturn(Optional.of(new MD_Category(3, "Category3")));

        // Call the controller method
        ResponseEntity<String> response = categoryController.deleteCategoriesByIds(idsToDelete);

        // Assertions for mixed case
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No categories found with IDs: [2]", response.getBody());
        verify(categoryRepository).deleteById(1);
        verify(categoryRepository).deleteById(3);
    }
}

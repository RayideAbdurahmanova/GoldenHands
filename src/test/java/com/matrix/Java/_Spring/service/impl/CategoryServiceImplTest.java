package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.exceptions.DataExistException;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.CategoryMapper;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.repository.CategoryRepository;
import com.matrix.Java._Spring.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private Category category;
    private Category parentCategory;
    private CategoryDto categoryDto;
    private CreateCategoryRequest createCategoryRequest;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1);
        category.setCategoryName("Old Bags");

        parentCategory = new Category();
        parentCategory.setId(2);
        parentCategory.setCategoryName("Accessories");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setCategoryName("Bags");
        categoryDto.setParentCategoryId(2L);

        createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setCategoryName("Bags");
        createCategoryRequest.setParentCategoryId(2L);


    }

    @AfterEach
    void tearDown() {
        category = null;
        parentCategory = null;
        categoryDto = null;
        createCategoryRequest = null;
    }

    @Test
    void getList() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDtoList(List.of(category))).thenReturn(List.of(categoryDto));

        List<CategoryDto> result = categoryService.getList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDto, result.get(0));

        verify(categoryRepository).findAll();
        verify(categoryMapper).toCategoryDtoList(List.of(category));
    }

    @Test
    void getById() {
        when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
        when(categoryMapper.toCategoryDtoGetById(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1);

        assertNotNull(result);
        assertEquals(categoryDto, result);
        verify(categoryRepository).findById(1);
        verify(categoryMapper).toCategoryDtoGetById(category);
    }

    @Test
    void getById_Empty() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> categoryService.getById(1));
    }

    @Test
    void create() {
        when(categoryRepository.existsByCategoryName("Bags")).thenReturn(false);
        when(categoryMapper.toCategoryAdd(createCategoryRequest)).thenReturn(category);
        when(categoryRepository.findById(2)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDtoGetById(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.create(createCategoryRequest);

        assertNotNull(result);
        assertEquals(categoryDto, result);

        verify(categoryRepository).existsByCategoryName("Bags");
        verify(categoryMapper).toCategoryAdd(createCategoryRequest);
        verify(categoryRepository).findById(2);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCategoryDtoGetById(category);
    }

    @Test
    void create_nameExist() {
        when(categoryRepository.existsByCategoryName("Bags")).thenReturn(true);

        assertThrows(DataExistException.class, () -> categoryService.create(createCategoryRequest));
    }

    @Test
    void create_ParentNotFound() {
        createCategoryRequest.setParentCategoryId(3L);
        when(categoryRepository.existsByCategoryName("Bags")).thenReturn(false);
        when(categoryRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> categoryService.create(createCategoryRequest));
    }

    @Test
    void updateToCategoryDto() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByCategoryName("Bags")).thenReturn(false);
        when(categoryRepository.findById(2)).thenReturn(Optional.of(parentCategory));
        doNothing().when(categoryMapper).updateCategoryFromDto(createCategoryRequest, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDtoGetById(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1, createCategoryRequest);

        assertNotNull(result);
        assertEquals(categoryDto, result);

        verify(categoryRepository).findById(1);
        verify(categoryRepository).existsByCategoryName("Bags");
        verify(categoryRepository).findById(2);
        verify(categoryRepository).save(category);
        verify(categoryMapper).updateCategoryFromDto(createCategoryRequest, category);
        verify(categoryMapper).toCategoryDtoGetById(category);
    }

    @Test
    void update_IdNotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> categoryService.update(1, createCategoryRequest));
    }

    @Test
    void delete() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(productRepository.countByCategory(category)).thenReturn(0);
        categoryService.delete(1);
        verify(categoryRepository).deleteById(1);
        verify(productRepository).countByCategory(category);
    }

    @Test
    void delete_IdNotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> categoryService.delete(1));
    }

    @Test
    void getSubcategories() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.findByParentCategory(category)).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDtoList(List.of(category))).thenReturn(List.of(categoryDto));

        List<CategoryDto> result = categoryService.getSubcategories(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository).findById(1);
        verify(categoryRepository).findByParentCategory(category);
        verify(categoryMapper).toCategoryDtoList(List.of(category));
    }

    @Test
    void getSubcategories_IdNotFound() {
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> categoryService.getSubcategories(2));
    }

    @Test
    void getSubcategories_InvalidParentId(){
        assertThrows(DataNotFoundException.class, () -> categoryService.getSubcategories(-1));
    }

}
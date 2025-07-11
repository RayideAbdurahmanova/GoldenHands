package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.exceptions.DataExistException;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.CategoryMapper;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.repository.CategoryRepository;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;


    @Override
    public List<CategoryDto> getList() {
        log.info("Starting retrieval of all categories");
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categoryMapper.toCategoryDtoList(categories);
        log.info("Finished retrieval {} category successfully", categoryDtos.size());
        return categoryDtos;
    }

    @Override
    public CategoryDto getById(Integer id) {
        if(id<=0){
            throw  new DataNotFoundException("Category ID cannot be less than 0");
        }
        log.info("Starting retrieval of a category by id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Data Not Found With ID:" + id + " CategoryID"));
        CategoryDto categoryDto=categoryMapper.toCategoryDtoGetById(category);
        log.info("Retrieved {} category successfully", categoryDto);
        return categoryDto;
    }

    @Override
    public CategoryDto create(CreateCategoryRequest createCategoryRequest) {
        log.info("Starting creating of category: {}", createCategoryRequest);
        if (Boolean.TRUE.equals(categoryRepository.existsByCategoryName(createCategoryRequest.getCategoryName()))) {
            throw new DataExistException("Category Name Exists");
        }
        Category category = categoryMapper.toCategoryAdd(createCategoryRequest);

        if(createCategoryRequest.getParentCategoryId() != null){
            Category parent=categoryRepository.findById(Math.toIntExact(createCategoryRequest.getParentCategoryId()))
                    .orElseThrow(()-> new DataNotFoundException("Parent category not found with: "+ createCategoryRequest.getParentCategoryId()));
            category.setParentCategory(parent);
        }else {
            category.setParentCategory(null);
        }

        Category saved = categoryRepository.save(category);
        CategoryDto categoryDto=categoryMapper.toCategoryDtoGetById(saved);
        log.info("Finished creation of product with id: {}", saved.getId());
        return categoryDto;
    }

    @Override
    public CategoryDto update(Integer id, CreateCategoryRequest createCategoryRequest) {
        log.info("Starting  update of category with ID: {} ", id);
        if(id<=0){
            throw  new DataNotFoundException("Category ID cannot be less than 0");
        }
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category with ID " + id + " not found"));
        if(!existing.getCategoryName().equals(createCategoryRequest.getCategoryName())&&
                categoryRepository.existsByCategoryName(createCategoryRequest.getCategoryName())) {
            throw new DataExistException("Category Name Exists");
        }
        categoryMapper.updateCategoryFromDto(createCategoryRequest,existing);

        if(createCategoryRequest.getParentCategoryId() != null){
            Category parent=categoryRepository.findById(Math.toIntExact(createCategoryRequest.getParentCategoryId()))
                    .orElseThrow(()->new DataNotFoundException("Parent category not found with "+ createCategoryRequest.getParentCategoryId()));
            existing.setParentCategory(parent);
        }else {
            existing.setParentCategory(null);
        }

        Category update = categoryRepository.save(existing);
        log.info("Updated category with ID {}", id);
        CategoryDto categoryDto=categoryMapper.toCategoryDtoGetById(update);
        log.info("Finished update of category with ID {} successfully", existing.getId());
        return categoryDto;
    }


    @Override
    public void delete(Integer id) {
        log.info("Starting  deletion of category with ID: {} ", id);
        if(id<=0){
            throw  new DataNotFoundException("Category ID cannot be less than 0");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category with ID " + id + " not found"));
        if (productRepository.countByCategory(category) > 0) {
            throw new DataExistException("Cannot delete category with ID " + id + " as it contains products");
        }

        categoryRepository.deleteById(id);
        log.info("Deleted category with ID {}", id);
        log.info("Finished deletion of category with ID: {} ", id);
    }

    @Override
    public List<CategoryDto> getSubcategories(Integer parentId) {
        log.info("Starting retrieval of a parent category with id: {}", parentId);
        if(parentId<=0){
            throw  new DataNotFoundException("Parent category ID cannot be less than 0");
        }
        Category parentCategory = categoryRepository.findById(parentId).orElseThrow(
                () -> new DataNotFoundException("Category not found with :" + parentId)
        );
        List<Category> subCategories=categoryRepository.findByParentCategory(parentCategory);
        List<CategoryDto> categoryDtos=categoryMapper.toCategoryDtoList(subCategories);


        log.info("Finished retrieval {} parent category successfully", categoryDtos.size());
        return categoryDtos;
    }

}

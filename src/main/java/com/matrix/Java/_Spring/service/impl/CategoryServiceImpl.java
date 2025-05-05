package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.CategoryMapper;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.repository.CategoryRepository;
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
    private  final CategoryMapper categoryMapper;


    @Override
    public List<CategoryDto> getList() {

        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getById(Integer id) {
        Category category=categoryRepository.findById(id)
                .orElseThrow(
                ()->new DataNotFoundException("Data Not Found With ID:"+id+" CategoryID"));
        return categoryMapper.toCategoryDtoGetById(category);

    }

    @Override
    public CategoryDto create(CreateCategoryRequest createCategoryRequest) {
        Category category=categoryMapper.toCategoryAdd(createCategoryRequest);
        if(createCategoryRequest.getParentCategoryId()!=null){
            Category parent =categoryRepository.findById(Math.toIntExact(createCategoryRequest.getParentCategoryId()))
                    .orElseThrow(()->new DataNotFoundException("Parent category not found with: "+ createCategoryRequest.getParentCategoryId()));
            category.setParentCategory(parent);
        }
        Category saved=categoryRepository.save(category);
        return categoryMapper.toCategoryDtoGetById(saved);

    }

    @Override
    public CategoryDto update(Integer id, CreateCategoryRequest createCategoryRequest) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category with ID " + id + " not found"));

        categoryMapper.updateCustomerFromDto(createCategoryRequest,category);

        if(createCategoryRequest.getParentCategoryId()!=null){
            Category parent=categoryRepository.findById(Math.toIntExact(createCategoryRequest.getParentCategoryId()))
                    .orElseThrow(()->new DataNotFoundException("Parent category not found with "+ createCategoryRequest.getParentCategoryId()));
            category.setParentCategory(parent);
        }else{
            category.setParentCategory(null);
        }

        Category update=categoryRepository.save(category);
        return  categoryMapper.toCategoryDtoGetById(update);
    }


    @Override
    public void delete(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category with ID " + id + " not found"));

        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getSubcategories(Integer parentId) {

        Category parentCategory=categoryRepository.findById(parentId).orElseThrow(
                ()->new DataNotFoundException("Category not found with :"+ parentId)
        );

        List<Category> subCategories=categoryRepository.findByParentCategory(parentCategory);
        List<CategoryDto> categoryDtos=categoryMapper.toCategoryDtoList(subCategories);
        return categoryDtos;
    }

    @Override
    public Integer getCountOfProductsInCategory(Integer id) {

        Category category=categoryRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Category not found with: "+ id)
        );

        return category.getProducts() ==null ? 0: category.getProducts().size();

    }





}

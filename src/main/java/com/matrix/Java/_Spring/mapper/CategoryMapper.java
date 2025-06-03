package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring")
public interface CategoryMapper {

    List<CategoryDto> toCategoryDtoList(List<Category> category);

    CategoryDto toCategoryDtoGetById(Category category);

    Category toCategoryAdd(CreateCategoryRequest createCategoryRequest);

    void updateCategoryFromDto(CreateCategoryRequest request, @MappingTarget Category category);
}

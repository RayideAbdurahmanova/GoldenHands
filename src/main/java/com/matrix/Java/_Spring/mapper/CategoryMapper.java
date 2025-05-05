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


    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    List<CategoryDto> toCategoryDtoList(List<Category> category);

    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    CategoryDto toCategoryDtoGetById(Category category);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toCategoryAdd(CreateCategoryRequest createCategoryRequest);

    @Mapping(target = "id", ignore = true)
    void updateCustomerFromDto(CreateCategoryRequest request, @MappingTarget Category category);
}

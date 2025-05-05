package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/CategoryList")
    public  List<CategoryDto> getCategoryList(){
        return categoryService.getList();
    }

    @GetMapping("/ByCategoryId/{id}")
    public CategoryDto getCategoryById(@PathVariable Integer id){
        return  categoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        return categoryService.create(createCategoryRequest);
    }

    @PutMapping("/update/{id}")
    public CategoryDto updateCategory(@PathVariable Integer id,
                                      @RequestBody CreateCategoryRequest createCategoryRequest) {
        return  categoryService.update(id, createCategoryRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }

    @GetMapping("ByParenId/{parentId}")
    public List<CategoryDto> getSubcategories(@PathVariable Integer parentId) {
        return categoryService.getSubcategories(parentId);
    }



    @GetMapping("ProductCount/{id}")
    public Integer getCountOfProductsInCategory(@PathVariable Integer id) {
        return categoryService.getCountOfProductsInCategory(id);
    }
}

package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER','USER')")
    public  List<CategoryDto> getCategoryList(){
        return categoryService.getList();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Integer id){
        return  categoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        return categoryService.create(createCategoryRequest);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Integer id,
                                      @RequestBody CreateCategoryRequest createCategoryRequest) {
        return  categoryService.update(id, createCategoryRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }

    @GetMapping("/parenId/{parentId}")
    public List<CategoryDto> getSubcategories(@PathVariable Integer parentId) {
        return categoryService.getSubcategories(parentId);
    }
}

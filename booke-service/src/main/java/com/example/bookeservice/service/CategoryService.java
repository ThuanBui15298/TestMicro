package com.example.bookeservice.service;


import com.example.bookeservice.dto.CategoryDTO;
import com.example.bookeservice.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(CategoryDTO categoryDTO, Long id);

    void deleteCategory(List<Long> id);

    List<Category> getAllCategory();

    List<Category> getDetail(Long id);
}

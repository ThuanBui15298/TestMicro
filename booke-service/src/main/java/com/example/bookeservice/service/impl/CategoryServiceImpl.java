package com.example.bookeservice.service.impl;

import com.example.bookeservice.dto.CategoryDTO;
import com.example.bookeservice.entity.Category;
import com.example.bookeservice.repository.CategoryRepository;
import com.example.bookeservice.service.CategoryService;
import com.example.bookeservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Configuration
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findByCodeAndDeleted(categoryDTO.getCode(), Constants.DONT_DELETE);
        if (categoryOptional.isPresent()) {
            throw new MessageDescriptorFormatException("Danh mục đã tồn tại");
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setCreatTime(new Date());
        category.setUpdateTime(new Date());
        category.setDeleted(Constants.DONT_DELETE);
        category.setStatus(Constants.STATUS_ACTIVE);
        categoryRepository.save(category);
        return category;
    }

    @Transactional
    @Override
    public Category updateCategory(CategoryDTO categoryDTO, Long id) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Danh mục không tồn tại");
        }

        Optional<Category> optionalCategory = categoryRepository.findByCodeAndDeleted(categoryDTO.getCode(), Constants.DONT_DELETE);
        if (optionalCategory.isPresent()) {
            throw new MessageDescriptorFormatException("Danh mục đã tồn tại");
        }

        Category category = categoryOptional.get();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setCreatTime(new Date());
        category.setUpdateTime(new Date());
        category.setDeleted(Constants.DONT_DELETE);
        category.setStatus(Constants.STATUS_ACTIVE);
        categoryRepository.save(category);
        return category;
    }

    @Transactional
    @Override
    public void deleteCategory(List<Long> id) {
        List<Category> categoryList = categoryRepository.findByIdInAndDeleted(id, Constants.DONT_DELETE);
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new MessageDescriptorFormatException(" Category id can not found!");
        }
        for (Category category : categoryList) {
            category.setDeleted(Constants.DELETED);
            category.setUpdateTime(new Date());
            categoryRepository.save(category);
        }
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getDetail(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            throw new MessageDescriptorFormatException(" category id can not found!");
        }
        return categoryRepository.findAllById(id);
    }
}

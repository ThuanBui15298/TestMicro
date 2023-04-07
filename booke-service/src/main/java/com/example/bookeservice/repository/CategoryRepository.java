package com.example.bookeservice.repository;

import com.example.bookeservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCodeAndDeleted(String code, Integer deleted);

    List<Category> findByIdInAndDeleted(List<Long> id, Integer deleted);

    List<Category> findAllById(Long id);
}

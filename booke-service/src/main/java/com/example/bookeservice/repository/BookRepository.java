package com.example.bookeservice.repository;

import com.example.bookeservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByIdInAndDeleted(List<Long> id, Integer deleted);

    Book findAllById(Long id);

    Optional<Book> findByCodeAndDeleted(String code, Integer deleted);
}

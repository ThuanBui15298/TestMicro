package com.example.bookeservice.service;

import com.example.bookeservice.dto.BookDTO;
import com.example.bookeservice.entity.Book;

import java.util.List;


public interface BookService {

    Book createBook(BookDTO bookDTO);

    Book updateBook(BookDTO bookDTO, Long id);

    void deleteBook(List<Long> id);

    List<Book> getAllBook();

    Book getDetail(Long id);

}

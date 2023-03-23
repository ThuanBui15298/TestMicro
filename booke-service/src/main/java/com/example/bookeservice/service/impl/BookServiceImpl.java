package com.example.bookeservice.service.impl;

import com.example.bookeservice.dto.BookDTO;
import com.example.bookeservice.entity.Book;
import com.example.bookeservice.repository.BookRepository;
import com.example.bookeservice.service.BookService;
import com.example.bookeservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Configuration
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book createBook(BookDTO bookDTO) {

        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        book.setCreatTime(new Date());
        book.setDeleted(Constants.DONT_DELETE);
        book.setStatus(Constants.STATUS_ACTIVE);
        book.setUpdateTime(new Date());
        bookRepository.save(book);
        return book;
    }

    @Override
    public Book updateBook(BookDTO bookDTO, Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Sách không tồn tại");
        }

        Book book = bookOptional.get();
        BeanUtils.copyProperties(bookDTO, book);
        book.setDeleted(Constants.DONT_DELETE);
        book.setStatus(bookDTO.getStatus());
        book.setUpdateTime(new Date());
        bookRepository.save(book);
        return book;
    }

    @Override
    public void deleteBook(List<Long> id) {
        List<Book> bookList = bookRepository.findByIdInAndDeleted(id, Constants.DONT_DELETE);
        if (CollectionUtils.isEmpty(bookList)) {
            throw new MessageDescriptorFormatException(" Book id can not found!");
        }
        for (Book book : bookList) {
            book.setDeleted(Constants.DELETED);
            book.setUpdateTime(new Date());
            bookRepository.save(book);
        }
    }

    @Override
    public List<Book> getAllBook() {

        return bookRepository.findAll();
    }

    @Override
    public Book getDetail(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new MessageDescriptorFormatException(" Book id can not found!");
        }
        return bookRepository.findAllById(id);
    }


}

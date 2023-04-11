package com.example.bookeservice.service.impl;

import com.example.bookeservice.clients.AuthClients;
import com.example.bookeservice.dto.BookDTO;
import com.example.bookeservice.dto.UserDTO;
import com.example.bookeservice.entity.Book;
import com.example.bookeservice.entity.Category;
import com.example.bookeservice.repository.BookRepository;
import com.example.bookeservice.repository.CategoryRepository;
import com.example.bookeservice.service.BookService;
import com.example.bookeservice.utils.Constants;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private  BookRepository bookRepository;

    private  CategoryRepository categoryRepository;

    @Autowired(required = false)
    private  AuthClients authClients;

    @Transactional
    @Override
    public Book createBook(BookDTO bookDTO) {

        Optional<Book> bookOptional = bookRepository.findByCodeAndDeleted(bookDTO.getCode(), Constants.DONT_DELETE);
        if (bookOptional.isPresent()) {
            throw new MessageDescriptorFormatException("Mã sách đã tồn tại");
        }

        Optional<Category> categoryOptional = categoryRepository.findByIdAndDeletedAndStatus(bookDTO.getCategoryId(), Constants.DONT_DELETE, Constants.STATUS_ACTIVE);
        if (categoryOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Danh mục không tồn tại");
        }
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        book.setCreatTime(new Date());
        book.setDeleted(Constants.DONT_DELETE);
        book.setStatus(Constants.STATUS_ACTIVE);
        book.setUpdateTime(new Date());
        bookRepository.save(book);
        return book;
    }

    @Transactional
    @Override
    public Book updateBook(BookDTO bookDTO, Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Sách không tồn tại");
        }

        Optional<Category> categoryOptional = categoryRepository.findByIdAndDeletedAndStatus(bookDTO.getCategoryId(), Constants.DONT_DELETE, Constants.STATUS_ACTIVE);
        if (categoryOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Danh mục không tồn tại");
        }

        Book book = bookOptional.get();
        Optional<Book> optionalBook = bookRepository.findByCodeAndDeleted(bookDTO.getCode(), Constants.DONT_DELETE);
        if (optionalBook.isEmpty() || book.getId().equals(optionalBook.get().getId())) {

            BeanUtils.copyProperties(bookDTO, book);
            book.setDeleted(Constants.DONT_DELETE);
            book.setStatus(bookDTO.getStatus());
            book.setUpdateTime(new Date());
            bookRepository.save(book);
        } else {
            throw new MessageDescriptorFormatException("Mã sách đã tồn tại");
        }
        return book;
    }

    @Transactional
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

//        Sort sortable = null;
//        if (sortType.equals("asc")) {
//            sortable = Sort.by(sortBy).ascending();
//        } else {
//            sortable = Sort.by(sortBy).descending();
//        }
//        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy),sortable);
//
//        Page<EmployeeEntity> pagedResult = repository.findAll(paging);
//
//        if(pagedResult.hasContent()) {
//            return pagedResult.getContent();
//        } else {
//            return new ArrayList<EmployeeEntity>();
//        }

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

    @Override
    public Object mapUser() {
//        authClients.getUser();
        RestTemplate rt = new RestTemplate();
        var result = rt.getForObject("http://localhost:9002/users", UserDTO.class);
        return result;
    }
}

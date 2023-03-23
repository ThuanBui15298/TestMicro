package com.example.bookeservice.controller;

import com.example.bookeservice.dto.BookDTO;
import com.example.bookeservice.entity.Book;
import com.example.bookeservice.response.ResponseData;
import com.example.bookeservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.bookeservice.response.ResponseDataStatus.ERROR;
import static com.example.bookeservice.response.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("book")
@RequiredArgsConstructor
@Tag(name = "BookApi", description = "Book Api")
public class BookApi {

    private final BookService bookService;

    @PostMapping("/create")
    @Operation(summary = "Create",
            description = "Create Book",
            tags = {"Book"})
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO) {
        try {
            Book book = bookService.createBook(bookDTO);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Create successful")
                    .data(book).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update",
            description = "Update Book",
            tags = {"Book"})
    public ResponseEntity<?> updateBook(@RequestBody BookDTO bookDTO,
                                        @PathVariable("id") Long id) {
        try {
            Book book = bookService.updateBook(bookDTO, id);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Update successful")
                    .data(book).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete",
            description = "Delete Book",
            tags = {"Book"})
    public ResponseEntity<?> deleteBook(@PathVariable("id") List<Long> id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Delete successful").build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Get",
            description = "Get Book",
            tags = {"Book"})
    public ResponseEntity<?> getAllBook() {
        try {
            List<Book> book = bookService.getAllBook();
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get All successful")
                    .data(book).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @GetMapping("/detail")
    @Operation(summary = "Get",
            description = "Get Book",
            tags = {"Book"})
    public ResponseEntity<?> getDetail(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get Detail successful")
                    .data(bookService.getDetail(id)).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }
}

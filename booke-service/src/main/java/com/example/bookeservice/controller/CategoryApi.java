package com.example.bookeservice.controller;

import com.example.bookeservice.dto.CategoryDTO;
import com.example.bookeservice.entity.Category;
import com.example.bookeservice.response.ResponseData;
import com.example.bookeservice.service.CategoryService;
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
@RequestMapping("category")
@RequiredArgsConstructor
@Tag(name = "CategoryApi", description = "Category Api")
public class CategoryApi {

    private final CategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "Create",
            description = "Create category",
            tags = {"Category"})
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = categoryService.createCategory(categoryDTO);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Create successful")
                    .data(category).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update",
            description = "Update Category",
            tags = {"Category"})
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO,
                                            @PathVariable("id") Long id) {
        try {
            Category category = categoryService.updateCategory(categoryDTO, id);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Update successful")
                    .data(category).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete",
            description = "Delete Category",
            tags = {"Category"})
    public ResponseEntity<?> deleteCategory(@PathVariable("id") List<Long> id) {
        try {
            categoryService.deleteCategory(id);
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
            description = "Get Category",
            tags = {"Category"})
    public ResponseEntity<?> getAll() {
        try {
            List<Category> categories = categoryService.getAllCategory();
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get All successful")
                    .data(categories).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @GetMapping("/detail")
    @Operation(summary = "Get",
            description = "Get Category",
            tags = {"Category"})
    public ResponseEntity<?> getDetail(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get Detail successful")
                    .data(categoryService.getDetail(id)).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }
}

package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.UserApp;
import com.example.userservice.response.ResponseData;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.userservice.response.ResponseDataStatus.ERROR;
import static com.example.userservice.response.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "UserApi", description = "UserApp Api")
public class UserApi {

    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Create",
            description = "Create UserApp",
            tags = {"UserApp"})
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserApp userApp = userService.createUser(userDTO);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Create successful")
                    .data(userApp).build(), OK);
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
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO,
                                        @PathVariable("id") Long id) {
        try {
            UserApp userApp = userService.updateUser(userDTO, id);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Update successful")
                    .data(userApp).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete",
            description = "Delete UserApp",
            tags = {"UserApp"})
    public ResponseEntity<?> deleteUser(@PathVariable("id") List<Long> id) {
        try {
            userService.deleteUser(id);
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
            description = "Get UserApp",
            tags = {"UserApp"})
    public ResponseEntity<?> getAllUser() {
        try {
            List<UserApp> book = userService.getAllUser();
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
            description = "Get UserApp",
            tags = {"UserApp"})
    public ResponseEntity<?> getDetail(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get Detail successful")
                    .data(userService.getDetail(id)).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

}

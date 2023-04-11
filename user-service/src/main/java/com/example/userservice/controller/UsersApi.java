package com.example.userservice.controller;

import com.example.userservice.dto.UsersDTO;
import com.example.userservice.entity.Users;
import com.example.userservice.response.Response;
import com.example.userservice.response.ResponseData;
import com.example.userservice.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static com.example.userservice.response.ResponseDataStatus.ERROR;
import static com.example.userservice.response.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "UsersApi", description = "Users Api")
public class UsersApi {

    private final UsersService usersService;

    @PostMapping("/create")
    @Operation(summary = "Create",
            description = "Create users",
            tags = {"Users"})
    public ResponseEntity<?> createUsers(@RequestBody UsersDTO usersDTO) {
        try {
            var user = usersService.createUsers(usersDTO);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Create successful")
                    .data(user).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update",
            description = "Update Users",
            tags = {"Users"})
    public ResponseEntity<?> updateBook(@RequestBody UsersDTO usersDTO,
                                        @PathVariable("id") Long id) {
        try {
            usersService.remove(id);
            var users = usersService.updateUsers(usersDTO, id);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Update successful")
                    .data(users).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete",
            description = "Delete users",
            tags = {"Users"})
    public ResponseEntity<?> deleteUsers(@PathVariable("id") List<Long> id) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "Delete successful!", usersService.deleteUsers(id), 1L));
    }


    @GetMapping
    @Operation(summary = "Get",
            description = "Get users",
            tags = {"users"})
    public ResponseEntity<?> getAllUsers() {
        try {
            List<Users> users = usersService.searchUsers();
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get All successful")
                    .data(users).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }


    @GetMapping("/detail")
    @Operation(summary = "Get",
            description = "Get User",
            tags = {"User"})
    public ResponseEntity<?> getDetail(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get Detail successful")
                    .data(usersService.getDetail(id)).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @GetMapping("/get-all-roles")
    @Operation(summary = "Get all roles",
            description = "Search by condition:",
            tags = {"Users"})
    public ResponseEntity<?> findAll() {
        var users = usersService.findAll();
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", users, 1L));
    }
}



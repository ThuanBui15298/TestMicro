package com.example.userservice.controller;

import com.example.userservice.dto.UsersDTO;
import com.example.userservice.response.BusinessException;
import com.example.userservice.response.Response;
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
    public ResponseEntity<Object> createUsers(@RequestBody UsersDTO usersDTO) throws BusinessException {
        return usersService.createUsers(usersDTO);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete",
            description = "Delete users",
            tags = {"Users"})
    public ResponseEntity<?> deleteUsers(@PathVariable("id") List<Long> id) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "Delete successful!", usersService.deleteUsers(id), 1L));
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get all users",
            description = "Search by condition: name, code",
            tags = {"Users"})
    public ResponseEntity<?> searchUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(defaultValue = "id") String sortBy,
                                         @RequestParam Integer status,
                                         @RequestParam(name = "sortType", required = false, defaultValue = "desc") String sortType,
                                         @RequestParam String search) {

        Sort sortable = null;
        if (sortType.equals("asc")) {
            sortable = Sort.by(sortBy).ascending();
        } else {
            sortable = Sort.by(sortBy).descending();
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, sortable);
        var data = usersService.searchUsers(paging, search, status);
        var content = data.getContent();
        var total = data.getTotalElements();
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", content, total));
    }

    @PostMapping("/reset-users/{id}")
    @Operation(summary = "Reset",
            description = "Rest users",
            tags = {"Users"})
    public ResponseEntity<?> resetPassWord(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "RestPassWord successful!", usersService.resetPassWord(id), 1L));
    }


    @GetMapping("/get-all-roles")
    @Operation(summary = "Get all roles",
            description = "Search by condition:",
            tags = {"Users"})
    public ResponseEntity<?> findAll() {
        var users = usersService.findAll();
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", users, 1L));
    }

    @PostMapping("/update-info/{id}")
    @Operation(summary = "Update",
            description = "Update users",
            tags = {"Users"})
    public ResponseEntity<Object> updateInfo(@RequestBody UsersDTO usersDTO,
                                             @PathVariable("id") Long id) {
        return usersService.updateInfo(usersDTO, id);
    }

    @PostMapping("/update-password/{email}")
    @Operation(summary = "Update",
            description = "Update users",
            tags = {"Users"})
    public ResponseEntity<?> updatePassWord(@RequestBody UsersDTO usersDTO,
                                @PathVariable("email") String email) {
        var users = usersService.updatePassWord(usersDTO, email);
        return users;
    }

}



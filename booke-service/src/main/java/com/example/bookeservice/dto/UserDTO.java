package com.example.bookeservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    private Long id;
    private String username;
    private String password;
    private List<String> roles;
    private String email;
}

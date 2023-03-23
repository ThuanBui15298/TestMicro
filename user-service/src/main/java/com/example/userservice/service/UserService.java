package com.example.userservice.service;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.UserApp;

import java.util.List;

public interface UserService {

    UserApp createUser(UserDTO userDTO);

    UserApp updateUser(UserDTO userDTO, Long id);

    void deleteUser(List<Long> id);

    List<UserApp> getAllUser();

    UserApp getDetail(Long id);

    CustomUserDetails loadUserByUserCode(String code);
}
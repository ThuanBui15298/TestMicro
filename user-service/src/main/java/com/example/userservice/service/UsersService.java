package com.example.userservice.service;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.UsersDTO;
import com.example.userservice.entity.Roles;
import com.example.userservice.entity.Users;
import com.example.userservice.response.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UsersService extends UserDetailsService {

    ResponseEntity<Object> createUsers(UsersDTO usersDTO) throws BusinessException;

    ResponseEntity<Object> updateUsers(UsersDTO usersDTO, Long id);

    Users deleteUsers(List<Long> id);

    Page<Map<String, Object>> searchUsers (Pageable pageable,  String search , Integer status);

    Users resetPassWord( Long id);

    CustomUserDetails loadUserByUserCode(String code);

    Users findByUserCode(String userCode);

    boolean checkIfValidOldPassword(Users user, String oldPassword);

    void changeAccountPassword(Users user, String password);

    List<Roles> findAll();


    ResponseEntity<Object> updateInfo(UsersDTO usersDTO, Long id);

    ResponseEntity<Object> updatePassWord(UsersDTO usersDTO, String email);

}

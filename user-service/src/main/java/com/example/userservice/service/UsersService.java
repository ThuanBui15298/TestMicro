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

    Users createUsers(UsersDTO usersDTO) throws BusinessException;

    Users updateUsers(UsersDTO usersDTO, Long id);

    Users deleteUsers(List<Long> id);

    List<Users> searchUsers ();

    CustomUserDetails loadUserByUserCode(String code);

    Users findByUserCode(String userCode);

    boolean checkIfValidOldPassword(Users user, String oldPassword);

    void changeAccountPassword(Users user, String password);

    List<Roles> findAll();

    Users getDetail(Long id);

    void remove(Long id);

}

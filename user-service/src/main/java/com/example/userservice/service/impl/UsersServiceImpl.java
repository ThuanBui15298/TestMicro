package com.example.userservice.service.impl;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.UsersDTO;
import com.example.userservice.entity.Roles;
import com.example.userservice.entity.Users;
import com.example.userservice.repository.RolesRepository;
import com.example.userservice.repository.UsersRepository;
import com.example.userservice.response.Response;
import com.example.userservice.service.UsersService;
import com.example.userservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final RolesRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Transactional
    @Override
    public ResponseEntity<Object> createUsers(UsersDTO usersDTO) {
        validRequest(usersDTO);
        Optional<Users> usersOptional = usersRepository.findByCodeAndDeleted(usersDTO.getCode(), Constants.DONT_DELETE);
        if (usersOptional.isPresent() || usersDTO.getCode().length() == 0) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Mã người dùng đã tồn tại"));
        }

        Optional<Users> usersOpt = usersRepository.findByEmailAndDeleted(usersDTO.getEmail(), Constants.DONT_DELETE);
        if (usersOpt.isPresent() || usersDTO.getEmail().length() == 0) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Email người dùng đã tồn tại"));
        }

        Optional<Users> OptUsers = usersRepository.findByPhoneAndDeleted(usersDTO.getPhone(), Constants.DONT_DELETE);
        if (OptUsers.isPresent() || usersDTO.getPhone().length() == 0) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Số điện thoại người dùng đã tồn tại"));
        }

        if (usersDTO.getCode().length() < 5) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Số kí tự phải lớn hơn 5"));
        }
        Users users = new Users();
        users.setName(usersDTO.getName());
        users.setCode(usersDTO.getCode());
        users.setNote(usersDTO.getNote());
        users.setUpdateTime(new Date());
        users.setCreatTime(new Date());
        users.setStatus(usersDTO.getStatus());
        users.setDeleted(Constants.DONT_DELETE);
        users.setPhone(usersDTO.getPhone());
        users.setPassword(passwordEncoder().encode(Constants.PASSWORD));
        users.setDateOfBirth(usersDTO.getDateOfBirth());
        users.setEmail(usersDTO.getEmail());
        usersRepository.save(users);

//        for (int i = 0; i < usersDTO.getRolesId().size(); i++) {
//            usersRepository.insert( users.getId(), usersDTO.getRolesId().get(i));
//        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", users, 1L));
    }

    @Transactional
    @Override
    public ResponseEntity<Object> updateUsers(UsersDTO usersDTO, Long id) {
        validRequest(usersDTO);
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isEmpty()) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Nhân viên không tồn tại"));
        }

        if (usersDTO.getCode().length() < 5) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "số kí tự nhập vào phải lớn hơn 5"));
        }

        Users user = usersOptional.get();

        Optional<Users> optionalUsers = usersRepository.findByCodeAndDeleted(usersDTO.getCode(), Constants.DONT_DELETE);
        if (optionalUsers.isEmpty() || user.getId().equals(optionalUsers.get().getId())) {
            Optional<Users> usersOpt = usersRepository.findByEmailAndDeleted(usersDTO.getEmail(), Constants.DONT_DELETE);
            if (usersOpt.isEmpty() || user.getId().equals(usersOpt.get().getId())) {
                Optional<Users> OptUsers = usersRepository.findByPhoneAndDeleted(usersDTO.getPhone(), Constants.DONT_DELETE);
                if (OptUsers.isEmpty() || user.getId().equals(OptUsers.get().getId())) {
                    user.setName(usersDTO.getName());
                    user.setCode(usersDTO.getCode());
                    user.setNote(usersDTO.getNote());
                    user.setUpdateTime(new Date());
                    user.setCreatTime(new Date());
                    user.setStatus(usersDTO.getStatus());
                    user.setDeleted(Constants.DONT_DELETE);
                    user.setPhone(usersDTO.getPhone());
                    user.setPassword(passwordEncoder().encode(Constants.PASSWORD));
                    user.setDateOfBirth(usersDTO.getDateOfBirth());
                    user.setEmail(usersDTO.getEmail());
                    usersRepository.save(user);

//                    for (int i = 0; i < usersDTO.getRolesId().size(); i++) {
//                        usersRepository.insert( user.getId(), usersDTO.getRolesId().get(i));
//                    }
                } else {
                    return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Số điện thoại người dùng đã tồn tại"));
                }
            } else {
                return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Email người dùng đã tồn tại"));
            }
        } else {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Mã nhân viên đã tồn tại"));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", usersOptional.get(), 1L));
    }

    @Transactional
    @Override
    public Users deleteUsers(List<Long> id) {
        List<Users> usersList = usersRepository.findAllByIdInAndDeleted(id, Constants.DONT_DELETE);
        if (CollectionUtils.isEmpty(usersList)) {
            throw new MessageDescriptorFormatException(" User id can not found!");
        }
        for (Users users : usersList) {
            users.setDeleted(Constants.DELETED);
            users.setUpdateTime(new Date());
            usersRepository.save(users);
        }
        return null;
    }

    @Override
    public Page<Map<String, Object>> searchUsers(Pageable pageable, String search, Integer status) {

        if (search != null) {
            if (status == null) {
                status = -1;
            }
            return usersRepository.findAllBySearch(pageable, search, status);
        } else {
            return usersRepository.findAllByUsers(pageable);
        }
    }

    @Transactional
    @Override
    public Users resetPassWord(Long id) {
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Can not found by id");
        }
        Users users = usersOptional.get();

        users.setPassword(passwordEncoder().encode(Constants.RESET_PASSWORD));
        usersRepository.save(users);
        return users;
    }

    @Override
    public CustomUserDetails loadUserByUserCode(String code) {
        return usersRepository.findByCode(code);
    }


    @Override
    public List<Roles> findAll() {
        return roleRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        Users user = usersRepository.findByUserNameOrCode(username);
        if (user == null) {
            throw new UsernameNotFoundException("Can not found username: " + username);
        }
        return new CustomUserDetails(user);
    }


    @Override
    public Users findByUserCode(String userCode) {
        return usersRepository.findByUserCodeAndStatus(userCode);
    }

    @Override
    public boolean checkIfValidOldPassword(Users user, String oldPassword) {
        return passwordEncoder().matches(oldPassword, user.getPassword());
    }

    @Override
    public void changeAccountPassword(Users user, String password) {
        user.setPassword(passwordEncoder().encode(password));
        usersRepository.save(user);
    }


    @Override
    public ResponseEntity<Object> updateInfo(UsersDTO usersDTO, Long id) {
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isEmpty()) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Nhân viên không tồn tại"));
        }
        Users user = usersOptional.get();

        user.setName(usersDTO.getName());
        user.setUpdateTime(new Date());
        user.setPhone(usersDTO.getPhone());
        user.setDateOfBirth(usersDTO.getDateOfBirth());
        user.setEmail(usersDTO.getEmail());
        user.setNote(usersDTO.getNote());
        usersRepository.save(user);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", usersOptional.get(), 1L));
    }

    @Override
    public ResponseEntity<Object> updatePassWord(UsersDTO usersDTO, String email) {
        Optional<Users> usersOptional = usersRepository.findAllByEmail(email);
        if (usersOptional.isEmpty()) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Email không tồn tại"));
        }
        Users users = usersOptional.get();

        users.setPassword(passwordEncoder().encode(usersDTO.getPassword()));
        usersRepository.save(users);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", usersOptional.get(), 1L));
    }


    private void validRequest(UsersDTO usersDTO) {
        if (usersDTO == null) {
            throw new MessageDescriptorFormatException("Request invalid");
        }

        if (usersDTO.getEmail() == null) {
            throw new MessageDescriptorFormatException("Users-email-null");
        }

        if (usersDTO.getPhone() == null) {
            throw new MessageDescriptorFormatException("Users-Phone-null");
        }

        if (usersDTO.getName() == null) {
            throw new MessageDescriptorFormatException("Users-Name-null");
        }

        if (usersDTO.getCode() == null) {
            throw new MessageDescriptorFormatException("Users-Code-null");
        }

    }

}
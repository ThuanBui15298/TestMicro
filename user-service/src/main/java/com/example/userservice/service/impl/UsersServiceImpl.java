package com.example.userservice.service.impl;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.UsersDTO;
import com.example.userservice.entity.Position;
import com.example.userservice.entity.Roles;
import com.example.userservice.entity.Users;
import com.example.userservice.repository.PositionRepository;
import com.example.userservice.repository.RolesRepository;
import com.example.userservice.repository.UsersRepository;
import com.example.userservice.service.UsersService;
import com.example.userservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final PositionRepository positionRepository;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Transactional
    @Override
    public Users createUsers(UsersDTO usersDTO) {

        validRequest(usersDTO);
        Optional<Users> usersOptional = usersRepository.findByCodeAndDeleted(usersDTO.getCode(), Constants.DONT_DELETE);
        if (usersOptional.isPresent() || usersDTO.getCode().length() == 0) {
            throw new MessageDescriptorFormatException("Mã người dùng đã tồn tại");
        }

        Optional<Users> usersOpt = usersRepository.findByEmailAndDeleted(usersDTO.getEmail(), Constants.DONT_DELETE);
        if (usersOpt.isPresent() || usersDTO.getEmail().length() == 0) {
            throw new MessageDescriptorFormatException("Email người dùng đã tồn tại");
        }

        Optional<Users> OptUsers = usersRepository.findByPhoneAndDeleted(usersDTO.getPhone(), Constants.DONT_DELETE);
        if (OptUsers.isPresent() || usersDTO.getPhone().length() == 0) {
            throw new MessageDescriptorFormatException("Số điện thoại người dùng đã tồn tại");
        }

        Optional<Position> positionOptional = positionRepository.findByIdAndDeletedAndStatus(usersDTO.getPositionId(), Constants.DONT_DELETE, Constants.STATUS_ACTIVE);
        if (positionOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Chức vụ không tồn tại");
        }

        Users users = new Users();
        BeanUtils.copyProperties(usersDTO,users);
        users.setUpdateTime(new Date());
        users.setCreatTime(new Date());
        users.setStatus(usersDTO.getStatus());
        users.setDeleted(Constants.DONT_DELETE);
        users.setPassword(passwordEncoder().encode(Constants.PASSWORD));
        usersRepository.save(users);

        for (int i = 0; i < usersDTO.getRolesId().size(); i++) {
            usersRepository.insert(users.getId(), usersDTO.getRolesId().get(i));
        }
        return users;
    }

    @Transactional
    @Override
    public Users updateUsers(UsersDTO usersDTO, Long id) {
        validRequest(usersDTO);
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Nhân viên không tồn tại");
        }

        Optional<Position> positionOptional = positionRepository.findByIdAndDeletedAndStatus(usersDTO.getPositionId(), Constants.DONT_DELETE, Constants.STATUS_ACTIVE);
        if (positionOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Chức vụ không tồn tại");
        }

        Users user = usersOptional.get();

        Optional<Users> optionalUsers = usersRepository.findByCodeAndDeleted(usersDTO.getCode(), Constants.DONT_DELETE);
        if (optionalUsers.isEmpty() || user.getId().equals(optionalUsers.get().getId())) {
            Optional<Users> usersOpt = usersRepository.findByEmailAndDeleted(usersDTO.getEmail(), Constants.DONT_DELETE);
            if (usersOpt.isEmpty() || user.getId().equals(usersOpt.get().getId())) {
                Optional<Users> OptUsers = usersRepository.findByPhoneAndDeleted(usersDTO.getPhone(), Constants.DONT_DELETE);
                if (OptUsers.isEmpty() || user.getId().equals(OptUsers.get().getId())) {
                    BeanUtils.copyProperties(usersDTO,user);
                    user.setUpdateTime(new Date());
                    user.setCreatTime(new Date());
                    user.setStatus(usersDTO.getStatus());
                    user.setDeleted(Constants.DONT_DELETE);
                    user.setPassword(passwordEncoder().encode(Constants.PASSWORD));
                    usersRepository.save(user);

                    for (int i = 0; i < usersDTO.getRolesId().size(); i++) {
                        usersRepository.insert(user.getId(), usersDTO.getRolesId().get(i));
                    }
                } else {
                    throw new MessageDescriptorFormatException("Số điện thoại người dùng đã tồn tại");
                }
            } else {
                throw new MessageDescriptorFormatException("Email người dùng đã tồn tại");
            }
        } else {
            throw new MessageDescriptorFormatException("Mã nhân viên đã tồn tại");
        }
        return user;
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
        return usersRepository.findAllBySearch(pageable, search, status);
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
    public UserDetails loadUserByUsername(String userCode) {
        Users user = usersRepository.findByCodeAndStatusAndDeleted(userCode, Constants.STATUS_ACTIVE, Constants.DONT_DELETE);
        if (user == null) {
            throw new MessageDescriptorFormatException("Can not found username: " + userCode);
        }
        return new CustomUserDetails(user);
    }

    @Override
    public Users findByUserCode(String userCode) {
        return usersRepository.findByCodeAndStatusAndDeleted(userCode, Constants.STATUS_ACTIVE, Constants.DONT_DELETE);
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
    public Users getDetail(Long id) {
        Optional<Users> usersOptional = usersRepository.findByIdAndDeletedAndStatus(id, Constants.DONT_DELETE, Constants.STATUS_ACTIVE);
        if (usersOptional.isEmpty()) {
            throw new MessageDescriptorFormatException(" User id can not found!");
        }
        return usersRepository.findAllById(id);
    }
    @Override
    public void remove(Long id) {
        usersRepository.deleteByUserId(id);
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
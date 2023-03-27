package com.example.userservice.service.impl;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.Position;
import com.example.userservice.entity.UserApp;
import com.example.userservice.repository.PositionRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.example.userservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Configuration
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final  PositionRepository positionRepository;


    @Transactional
    @Override
    public UserApp createUser(UserDTO userDTO) {
        Optional<Position> positionOptional = positionRepository.findById(userDTO.getPositionId());
        if (positionOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Position id can not found!");
        }
        Optional<UserApp> userOptional = userRepository.findByCodeAndDeleted(userDTO.getCode(), Constants.DONT_DELETE);
        if (userOptional.isPresent()) {
            throw new MessageDescriptorFormatException("Mã người dùng đã tồn tại");
        }

        UserApp userApp = new UserApp();
        BeanUtils.copyProperties(userDTO, userApp);
        userApp.setCreatTime(new Date());
        userApp.setDeleted(Constants.DONT_DELETE);
        userApp.setStatus(Constants.STATUS_ACTIVE);
        userApp.setUpdateTime(new Date());
        userRepository.save(userApp);
        return userApp;
    }

    @Transactional
    @Override
    public UserApp updateUser(UserDTO userDTO, Long id) {

        Optional<Position> positionOptional = positionRepository.findById(userDTO.getPositionId());
        if (positionOptional.isEmpty()) {
            throw new MessageDescriptorFormatException(" Position id can not found!");
        }

        Optional<UserApp> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Người dùng không tồn tại");
        }

        UserApp userApp = userOptional.get();
        BeanUtils.copyProperties(userDTO, userApp);
        userApp.setDeleted(Constants.DONT_DELETE);
        userApp.setStatus(Constants.STATUS_ACTIVE);
        userApp.setUpdateTime(new Date());
        userRepository.save(userApp);
        return userApp;
    }

    @Override
    public void deleteUser(List<Long> id) {
        List<UserApp> userAppList = userRepository.findByIdInAndDeleted(id, Constants.DONT_DELETE);
        if (CollectionUtils.isEmpty(userAppList)) {
            throw new MessageDescriptorFormatException(" Book id can not found!");
        }
        for (UserApp userApp : userAppList) {
            userApp.setDeleted(Constants.DELETED);
            userApp.setUpdateTime(new Date());
            userRepository.save(userApp);
        }
    }

    @Override
    public List<UserApp> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserApp getDetail(Long id) {
        Optional<UserApp> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new MessageDescriptorFormatException(" UserApp id can not found!");
        }
        return userRepository.findAllById(id);
    }

    @Override
    public CustomUserDetails loadUserByUserCode(String code) {
        return userRepository.findByCode(code);
    }
}

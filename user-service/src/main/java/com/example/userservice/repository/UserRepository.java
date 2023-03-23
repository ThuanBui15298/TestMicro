package com.example.userservice.repository;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserApp, Long> {

    List<UserApp> findByIdInAndDeleted(List<Long> id, Integer deleted);

    UserApp findAllById(Long id);

    Optional<UserApp> findByCodeAndDeleted(String code, Integer deleted);

    CustomUserDetails findByCode(String code);
}

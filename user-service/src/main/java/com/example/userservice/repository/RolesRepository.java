package com.example.userservice.repository;

import com.example.userservice.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Roles findByNameAndIdentification(String name, String identification);

    List<Roles> findAll();
}

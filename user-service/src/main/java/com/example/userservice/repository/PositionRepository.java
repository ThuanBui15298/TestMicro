package com.example.userservice.repository;

import com.example.userservice.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByIdInAndDeleted(List<Long> id, Integer deleted);

    Optional<Position> findByIdAndDeletedAndStatus(Long id, Integer deleted, Integer status);
}

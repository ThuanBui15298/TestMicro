package com.example.userservice.service;

import com.example.userservice.dto.PositionDTO;
import com.example.userservice.entity.Position;

import java.util.List;

public interface PositionService {

    Position createPosition(PositionDTO positionDTO);

    Position updatePosition(PositionDTO positionDTO, Long id);

    void deletePosition(List<Long> id);

    List<Position> getAllPosition();

}

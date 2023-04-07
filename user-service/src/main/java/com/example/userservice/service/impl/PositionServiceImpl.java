package com.example.userservice.service.impl;

import com.example.userservice.dto.PositionDTO;
import com.example.userservice.entity.Position;
import com.example.userservice.repository.PositionRepository;
import com.example.userservice.service.PositionService;
import com.example.userservice.utils.Constants;
import lombok.RequiredArgsConstructor;
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
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Transactional
    @Override
    public Position createPosition(PositionDTO positionDTO) {

        Position position = new Position();
        BeanUtils.copyProperties(positionDTO, position);
        position.setCreatTime(new Date());
        position.setDeleted(Constants.DONT_DELETE);
        position.setStatus(Constants.STATUS_ACTIVE);
        position.setUpdateTime(new Date());
        positionRepository.save(position);
        return position;
    }

    @Transactional
    @Override
    public Position updatePosition(PositionDTO positionDTO, Long id) {
        Optional<Position> positionOptional = positionRepository.findById(id);
        if (positionOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("Chức vụ không tồn tại");
        }

        Position position = positionOptional.get();
        BeanUtils.copyProperties(positionDTO, position);
        position.setDeleted(Constants.DONT_DELETE);
        position.setStatus(Constants.STATUS_ACTIVE);
        position.setUpdateTime(new Date());
        positionRepository.save(position);
        return position;
    }

    @Override
    public void deletePosition(List<Long> id) {
        List<Position> positionList = positionRepository.findByIdInAndDeleted(id, Constants.DONT_DELETE);
        if (CollectionUtils.isEmpty(positionList)) {
            throw new MessageDescriptorFormatException(" Position id can not found!");
        }
        for (Position position : positionList) {
            position.setDeleted(Constants.DELETED);
            position.setUpdateTime(new Date());
            positionRepository.save(position);
        }
    }

    @Override
    public List<Position> getAllPosition() {
        return positionRepository.findAll();
    }
}

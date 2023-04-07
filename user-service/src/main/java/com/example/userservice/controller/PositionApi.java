package com.example.userservice.controller;

import com.example.userservice.dto.PositionDTO;
import com.example.userservice.entity.Position;
import com.example.userservice.response.ResponseData;
import com.example.userservice.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.userservice.response.ResponseDataStatus.ERROR;
import static com.example.userservice.response.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("position")
@RequiredArgsConstructor
@Tag(name = "PositionApi", description = "Position Api")
public class PositionApi {

    private final PositionService positionService;

    @PostMapping("/create")
    @Operation(summary = "Create",
            description = "Create Position",
            tags = {"Position"})
    public ResponseEntity<?> createPosition(@RequestBody PositionDTO positionDTO) {
        try {
            Position position = positionService.createPosition(positionDTO);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Create successful")
                    .data(position).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update",
            description = "Update position",
            tags = {"Position"})
    public ResponseEntity<?> updatePosition(@RequestBody PositionDTO positionDTO,
                                            @PathVariable("id") Long id) {
        try {
            Position position = positionService.updatePosition(positionDTO, id);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Update successful")
                    .data(position).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete",
            description = "Delete Position",
            tags = {"Position"})
    public ResponseEntity<?> deletePosition(@PathVariable("id") List<Long> id) {
        try {
            positionService.deletePosition(id);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Delete successful").build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get",
            description = "Get Position",
            tags = {"Position"})
    public ResponseEntity<?> getAllPosition() {
        try {
            List<Position> positionList = positionService.getAllPosition();
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get All successful")
                    .data(positionList).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

}

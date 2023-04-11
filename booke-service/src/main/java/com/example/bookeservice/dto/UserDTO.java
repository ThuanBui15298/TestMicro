package com.example.bookeservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    private String code;

    private String  name;

    private String phone;

    private String email;

    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String username;

    private List<Long> rolesId;

    private Integer status;

    private Long positionId;
}

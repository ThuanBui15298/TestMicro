package com.example.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {

    @NotEmpty(message="Mã nhân viên không được trống")
    private String code;

    @NotEmpty(message="Họ tên không được trống")
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

    private String password;

}

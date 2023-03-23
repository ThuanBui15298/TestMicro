package com.example.userservice.dto;

import lombok.Data;

@Data
public class UserDTO {

    private  String userName;

    private  String code;

    private String phone;

    private String address;

    private String email;

    private String note;

    private Long positionId;

    private String passWord;

    private  String token;

}

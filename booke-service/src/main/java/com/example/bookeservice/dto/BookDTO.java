package com.example.bookeservice.dto;

import lombok.Data;

@Data
public class BookDTO {

    private  String name;

    private  String code;

    private String content;

    private Integer status;
}

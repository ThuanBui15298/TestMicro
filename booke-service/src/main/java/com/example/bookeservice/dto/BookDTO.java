package com.example.bookeservice.dto;

import lombok.Data;

@Data
public class BookDTO {

    private  String name;

    private  String code;

    private String content;

    private Integer status;

    private Long userId;

    private  Long categoryId;

    private  Integer quality;

    private String author;
}

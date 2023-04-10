package com.example.bookeservice.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book extends PersistableEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters long")
    private  String name;

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 255 characters long")
    private  String code;

    @Size(min = 2, max = 500, message = "Name must be between 2 and 500 characters long")
    private String content;

    private  Integer quality;

    private String author;

    private Long userId;

    private  Long categoryId;
}

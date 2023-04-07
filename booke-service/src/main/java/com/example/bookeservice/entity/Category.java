package com.example.bookeservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Category")
public class Category extends  PersistableEntity {
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
}

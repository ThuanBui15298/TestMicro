package com.example.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Roles{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private  String identification;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "role", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<Users> user;

    public Roles(String roleName, String identification) {
    }
}
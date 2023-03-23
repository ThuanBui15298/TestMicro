package com.example.userservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserApp extends PersistableEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters long")
    private String userName;

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 255 characters long")
    private String code;

    @Size(min = 10, max = 12, message = "Name must be between 2 and 255 characters long")
    private String phone;

    @Size(min = 5, max = 50, message = "Name must be between 2 and 255 characters long")
    private String address;

    @Size(min = 5, max = 25, message = "Name must be between 2 and 255 characters long")
    private String email;

    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters long")
    private String note;

    private Long positionId;

    private String token;

    private String password;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Role> role = new HashSet<>();
}

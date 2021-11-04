package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, name = "first_name")
    @NotBlank
    private String firstName;
    @Column(nullable = false, name = "last_name")
    @NotBlank
    private String lastName;
    @Column(nullable = false)
    @NotBlank
    private String password;
    @Column(nullable = false)
    @NotBlank
    private String email;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private boolean isEmailVerified;
    private UUID token;
}

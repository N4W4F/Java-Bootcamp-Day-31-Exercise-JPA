package com.example.ecommercestore.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(16) not null unique")
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 6, message = "Username must be more than 5 characters")
    private String username;

    @Column(columnDefinition = "varchar(16) not null")
    @NotEmpty(message = "User Password cannot be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "Password must be at least 6 characters long, and contain both letters and digits")
    private String password;

    @Column(columnDefinition = "varchar(50) not null unique")
    @NotEmpty(message = "User Email cannot be empty")
    @Email(message = "User Email must be a valid email")
    private String email;

    @Column(columnDefinition = "varchar(10) not null")
    @NotEmpty(message = "User Role cannot be empty")
    @Pattern(regexp = "^(Admin|Customer)", message = "User Role must be either Admin or Customer")
    private String role;

    @Column(columnDefinition = "decimal not null")
    @NotNull(message = "User Balance cannot be empty")
    @Positive(message = "User Balance must be positive number")
    private Double balance;
}


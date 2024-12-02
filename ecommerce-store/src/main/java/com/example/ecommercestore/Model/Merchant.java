package com.example.ecommercestore.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Merchant Name cannot be empty")
    @Size(min = 4, message = "Merchant Name must be more than 3 characters")
    @Column(columnDefinition = "varchar(16) not null unique")
    private String name;
}

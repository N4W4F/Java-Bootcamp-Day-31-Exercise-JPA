package com.example.ecommercestore.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(16) not null")
    @NotEmpty(message = "Product Name cannot be empty")
    @Size(min = 4, message = "Product Name must be more than 3 characters")
    private String name;

    @Column(columnDefinition = "decimal not null")
    @NotNull(message = "Product Price cannot be empty")
    @Positive(message = "Product Price must be a positive number")
    private Double price;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Category ID cannot be empty")
    private Integer category_id;
}
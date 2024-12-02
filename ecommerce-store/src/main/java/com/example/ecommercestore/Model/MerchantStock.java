package com.example.ecommercestore.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MerchantStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Product ID for Merchant Stock cannot be empty")
    private Integer product_id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Merchant ID for Merchant Stock cannot be empty")
    private Integer merchant_id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Merchant Stock cannot be empty")
    @Min(value = 10, message = "Merchant Stock must be 10 at start")
    private Integer stock;
}

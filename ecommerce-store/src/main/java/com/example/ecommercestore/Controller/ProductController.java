package com.example.ecommercestore.Controller;

import com.example.ecommercestore.ApiResponse.ApiResponse;
import com.example.ecommercestore.Model.Product;
import com.example.ecommercestore.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity getAllProducts() {
        if (productService.getAllProducts() == null)
            return ResponseEntity.status(400).body(new ApiResponse("There are no products yet"));

        return ResponseEntity.status(200).body(productService.getAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (productService.addProduct(product).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Product has been added successfully"));

        else if (productService.addProduct(product).equals("null"))
            return ResponseEntity.status(400).body(new ApiResponse("There are no categories yet"));

        else if (productService.addProduct(product).equals("invalid category"))
            return ResponseEntity.status(400).body(new ApiResponse("Category with ID: " + product.getCategory_id() + " was not found"));

        return ResponseEntity.status(400).body(new ApiResponse("Product ID is already used"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id, @RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (productService.updateProduct(id, product).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Product with ID: " + id + " has been updated successfully"));

        else if (productService.updateProduct(id, product).equals("invalid category"))
            return ResponseEntity.status(400).body(new ApiResponse("Category with ID: " + product.getCategory_id() + " was not found"));

        return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + id + " was not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id) {
        if (productService.deleteProduct(id))
            return ResponseEntity.status(200).body(new ApiResponse("Product with ID: " + id + " has been deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + id + " was not found"));
    }

}

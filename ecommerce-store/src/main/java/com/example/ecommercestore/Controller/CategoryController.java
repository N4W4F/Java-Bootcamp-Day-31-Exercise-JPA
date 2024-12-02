package com.example.ecommercestore.Controller;

import com.example.ecommercestore.ApiResponse.ApiResponse;
import com.example.ecommercestore.Model.Category;
import com.example.ecommercestore.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity getAllCategories() {
        if (categoryService.getAllCategories() == null)
            return ResponseEntity.status(400).body(new ApiResponse("There are no categories yet"));

        return ResponseEntity.status(200).body(categoryService.getAllCategories());
    }

    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody @Valid Category category, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        categoryService.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("Category has been added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory(@PathVariable Integer id, @RequestBody @Valid Category category, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (categoryService.updateCategory(id, category))
            return ResponseEntity.status(200).body(new ApiResponse("Category with ID: " + id + " has been updated successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Category with ID: " + id + " was not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable Integer id) {
        if (categoryService.deleteCategory(id))
            return ResponseEntity.status(200).body(new ApiResponse("Category with ID: " + id + " has been deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Category with ID: " + id + " was not found"));
    }
}

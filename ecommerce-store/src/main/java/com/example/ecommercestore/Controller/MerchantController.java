package com.example.ecommercestore.Controller;

import com.example.ecommercestore.ApiResponse.ApiResponse;
import com.example.ecommercestore.Model.Merchant;
import com.example.ecommercestore.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity getAllMerchants() {
        if (merchantService.getAllMerchants() == null)
            return ResponseEntity.status(400).body(new ApiResponse("There are no merchants yet"));

        return ResponseEntity.status(200).body(merchantService.getAllMerchants());
    }

    @PostMapping("/add")
    public ResponseEntity addMerchant(@RequestBody @Valid Merchant merchant, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        merchantService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant has been added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchant(@PathVariable Integer id, @RequestBody @Valid Merchant merchant, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (merchantService.updateMerchant(id, merchant))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant with ID: " + id + " has been updated successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + id + " was not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchant(@PathVariable Integer id) {
        if (merchantService.deleteMerchant(id))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant with ID: " + id + " has been deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + id + " was not found"));
    }
}

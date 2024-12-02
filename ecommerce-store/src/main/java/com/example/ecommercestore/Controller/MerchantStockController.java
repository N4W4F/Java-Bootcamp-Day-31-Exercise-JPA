package com.example.ecommercestore.Controller;

import com.example.ecommercestore.ApiResponse.ApiResponse;
import com.example.ecommercestore.Model.MerchantStock;
import com.example.ecommercestore.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchantstocks")
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity getAllMerchantStocks() {
        if (merchantStockService.getAllMerchantStock() == null)
            return ResponseEntity.status(400).body(new ApiResponse("There are no merchant stocks yet"));

        return ResponseEntity.status(200).body(merchantStockService.getAllMerchantStock());
    }

    @PostMapping("/add")
    public ResponseEntity addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (merchantStockService.addMerchantStock(merchantStock).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock has been added successfully"));

        else if (merchantStockService.addMerchantStock(merchantStock).equals("invalid product"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + merchantStock.getProduct_id() + " was not found"));

        else if (merchantStockService.addMerchantStock(merchantStock).equals("invalid merchant"))
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + merchantStock.getMerchant_id() + " was not found"));

        return ResponseEntity.status(400).body(new ApiResponse("MerchantStock ID is already used"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchantStock(@PathVariable Integer id, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (merchantStockService.updateMerchantStock(id, merchantStock).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock with ID: " + id + " has been updated successfully"));

        else if (merchantStockService.updateMerchantStock(id, merchantStock).equals("invalid product"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + merchantStock.getProduct_id() + " was not found"));

        else if (merchantStockService.updateMerchantStock(id, merchantStock).equals("invalid merchant"))
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + merchantStock.getMerchant_id() + " was not found"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock with ID: " + id + " was not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchantStock(@PathVariable Integer id) {
        if (merchantStockService.deleteMerchantStock(id))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock with ID: " + id + " has been deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock with ID: " + id + " was not found"));
    }

    // 9- Extra Endpoint 'restockProduct' is used to restock a current MerchantStock by any quantity
    @PutMapping("/restock/{merchant_id}/{product_id}")
    public ResponseEntity restockProduct(@PathVariable Integer merchant_id, @PathVariable Integer product_id, @RequestBody Integer quantity) {
        if (merchantStockService.restockProduct(merchant_id, product_id, quantity).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Product has been restocked successfully"));

        else if (merchantStockService.restockProduct(merchant_id, product_id, quantity).equals("invalid quantity"))
            return ResponseEntity.status(400).body(new ApiResponse("Quantity must be a positive integer"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock does not exist"));
    }
}

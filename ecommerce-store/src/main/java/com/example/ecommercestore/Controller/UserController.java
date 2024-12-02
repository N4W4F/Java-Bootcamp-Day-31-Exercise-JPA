package com.example.ecommercestore.Controller;

import com.example.ecommercestore.ApiResponse.ApiResponse;
import com.example.ecommercestore.Model.User;
import com.example.ecommercestore.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity getUsers() {
        if (userService.getUsers() == null)
            return ResponseEntity.status(400).body(new ApiResponse("There are no users yet"));

        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User has been added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (userService.updateUser(id, user))
            return ResponseEntity.status(200).body(new ApiResponse("User with ID: " + id + " has been updated successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + id + " was not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        if (userService.deleteUser(id))
            return ResponseEntity.status(200).body(new ApiResponse("User with ID: " + id + " has been deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + id + " was not found"));
    }

    @PostMapping("/buy-product/{user_id}/{merchant_id}/{product_id}")
    public ResponseEntity buyProduct(@PathVariable Integer user_id, @PathVariable Integer merchant_id, @PathVariable Integer product_id) {
        if (userService.buyProduct(user_id, merchant_id, product_id).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("You have bought the product successfully"));

        else if (userService.buyProduct(user_id, merchant_id, product_id).equals("invalid merchant"))
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + merchant_id + " was not found"));

        else if (userService.buyProduct(user_id, merchant_id, product_id).equals("invalid product"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + product_id + " was not found"));

        else if (userService.buyProduct(user_id, merchant_id, product_id).equals("out of stock"))
            return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock"));

        else if (userService.buyProduct(user_id, merchant_id, product_id).equals("no balance"))
            return ResponseEntity.status(400).body(new ApiResponse("You don't have enough balance to buy this product"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }

    // 1- Extra Endpoint 'transferBalance' is used to transfer money from user to another
    @PutMapping("/transfer-balance/{sender_id}/{receiver_id}")
    public ResponseEntity transferBalance(@PathVariable Integer sender_id, @PathVariable Integer receiver_id, @RequestBody Double amount) {
        if (userService.transferBalance(sender_id, receiver_id, amount).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Amount has been transferred successfully"));

        else if (userService.transferBalance(sender_id, receiver_id, amount).equals("no balance"))
            return ResponseEntity.status(400).body(new ApiResponse("You don't have enough balance to transfer this amount"));

        else if (userService.transferBalance(sender_id, receiver_id, amount).equals("invalid receiver"))
            return ResponseEntity.status(400).body(new ApiResponse("User (Receiver) with ID: " + receiver_id + " was not found"));

        return ResponseEntity.status(400).body(new ApiResponse("User (Sender) with ID: " + sender_id + " was not found"));
    }

    // 2- Extra Endpoint 'resetPassword' is used to reset a user's password
    @PutMapping("/reset-password/{user_id}")
    public ResponseEntity resetPassword(@PathVariable Integer user_id, @RequestBody String password) {
        if (userService.resetPassword(user_id, password).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Password has been changed successfully"));

        else if (userService.resetPassword(user_id, password).equals("invalid password"))
            return ResponseEntity.status(400).body(new ApiResponse("Password must be at least 6 characters long, and contain both letters and digits"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }

    // 3- Extra Endpoint 'refundPurchase' is used to return the money back to user after a valid refund
    @PutMapping("/refund-purchase/{user_id}/{merchant_id}/{product_id}")
    public ResponseEntity refundPurchase(@PathVariable Integer user_id, @PathVariable Integer merchant_id, @PathVariable Integer product_id) {
        if (userService.refundPurchase(user_id, merchant_id, product_id).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Purchase has been refunded successfully"));

        else if (userService.refundPurchase(user_id, merchant_id, product_id).equals("invalid merchant"))
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + merchant_id + " was not found"));

        else if (userService.refundPurchase(user_id, merchant_id, product_id).equals("invalid product"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + product_id + " was not found"));

        else if (userService.refundPurchase(user_id, merchant_id, product_id).equals("not found"))
            return ResponseEntity.status(400).body(new ApiResponse("You did not order this product"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }

    // 4- Extra Endpoint 'addToWishlist' allows the user to add a product by its ID to their wishlist
    @PutMapping("/add-to-wishlist/{user_id}/{product_id}")
    public ResponseEntity addToWishlist(@PathVariable Integer user_id, @PathVariable Integer product_id) {
        if (userService.addToWishlist(user_id, product_id).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Product has been added to your wishlist successfully"));

        else if (userService.addToWishlist(user_id, product_id).equals("already wished"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + product_id + " is already in your wishlist"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }

    // 5- Extra Endpoint 'removeFromWishlist' allows the user to remove a product by its ID to
    @DeleteMapping("/remove-from-wishlist/{user_id}/{product_id}")
    public ResponseEntity removeFromWishlist(@PathVariable Integer user_id, @PathVariable Integer product_id) {
        if (userService.removeFromWishlist(user_id, product_id).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Product has been removed from your wishlist successfully"));

        else if (userService.removeFromWishlist(user_id, product_id).equals("invalid product"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + product_id + " was not found"));

        else if (userService.removeFromWishlist(user_id, product_id).equals("not found"))
            return ResponseEntity.status(400).body(new ApiResponse("Product is not in your wishlist"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }

    // 6- Extra Endpoint 'addToCart' allows the user to add a product to their cart
    @PutMapping("/add-to-cart/{user_id}/{product_id}")
    public ResponseEntity addToCart(@PathVariable Integer user_id, @PathVariable Integer product_id) {
        if (userService.addToCart(user_id, product_id).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Product has been added to your cart"));

        else if (userService.addToCart(user_id, product_id).equals("invalid product"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + product_id + " was not found"));

        else if (userService.addToCart(user_id, product_id).equals("already in cart"))
            return ResponseEntity.status(400).body(new ApiResponse("Product is already in your cart"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }

    // 7- Extra Endpoint 'removeFromCart' allows the user to remove a product from their cart
    @DeleteMapping("/remove-from-cart/{user_id}/{product_id}")
    public ResponseEntity removeFromCart(@PathVariable Integer user_id, @PathVariable Integer product_id) {
        if (userService.removeFromCart(user_id, product_id).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Product has been removed from your cart"));

        else if (userService.removeFromCart(user_id, product_id).equals("invalid product"))
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + product_id + " was not found"));

        else if (userService.removeFromCart(user_id, product_id).equals("not found"))
            return ResponseEntity.status(400).body(new ApiResponse("Product is not in your cart"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }

    // 8- Extra Endpoint 'placeOrder' allows the user to buy the products in their cart
    @PostMapping("/place-order/{user_id}")
    public ResponseEntity placeOrder(@PathVariable Integer user_id) {
        if (userService.placeOrder(user_id).equals("ok"))
            return ResponseEntity.status(200).body(new ApiResponse("Order has been placed successfully"));

        else if (userService.placeOrder(user_id).equals("no balance"))
            return ResponseEntity.status(400).body(new ApiResponse("You don't have enough balance to place order"));

        else if (userService.placeOrder(user_id).equals("empty"))
            return ResponseEntity.status(400).body(new ApiResponse("Your cart is empty"));

        return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + user_id + " was not found"));
    }
}
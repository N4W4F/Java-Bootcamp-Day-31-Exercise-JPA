package com.example.ecommercestore.Service;

import com.example.ecommercestore.Model.*;
import com.example.ecommercestore.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantStockRepository merchantStockRepository;
    private final ProductRepository productRepository;

    private final ArrayList<Product> wishlist = new ArrayList<>();
    private final ArrayList<Product> cart = new ArrayList<>();
    private final ArrayList<Product> orderHistory = new ArrayList<>();

    public List<User> getUsers() {
        if (userRepository.findAll().isEmpty())
            return null;

        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public Boolean updateUser(Integer id, User user) {
        User oldUser = findUserById(id);
        if (oldUser == null) return false;

        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        oldUser.setRole(user.getRole());
        oldUser.setBalance(user.getBalance());
        userRepository.save(oldUser);
        return true;
    }

    public Boolean deleteUser(Integer id) {
        User oldUser = findUserById(id);
        if (oldUser == null) return false;

        userRepository.delete(oldUser);
        return true;
    }

    public String buyProduct(Integer user_id, Integer merchant_id, Integer product_id) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        Merchant merchant = findMerchantById(merchant_id);
        if (merchant == null) return "invalid merchant";

        Product product = findProductById(product_id);
        if (product == null) return "invalid product";

        for (MerchantStock ms : merchantStockRepository.findAll()) {
            if (ms.getProduct_id().equals(product_id) && ms.getMerchant_id().equals(merchant_id)) {
                if (user.getBalance() >= product.getPrice()) {
                    if (ms.getStock() > 0) {
                        ms.setStock(ms.getStock() - 1);
                        user.setBalance(user.getBalance() - product.getPrice());
                        userRepository.save(user);
                        orderHistory.add(product);
                        return "ok";
                    } else return "out of stock";
                } else return "no balance";
            }
        }
        return "out of stock";
    }

    // 1- Extra Endpoint 'transferBalance' is used to transfer money from user to another
    public String transferBalance(Integer sender_id, Integer receiver_id, Double amount) {
        User sender = findUserById(sender_id);
        if (sender == null) return "invalid sender";

        User receiver = findUserById(receiver_id);
        if (receiver == null) return "invalid receiver";

        if (sender.getBalance() >= amount) {
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);
            userRepository.save(sender);
            userRepository.save(receiver);
            return "ok";
        }
        return "no balance";
    }

    // 2- Extra Endpoint 'resetPassword' is used to reset a user's password
    public String resetPassword(Integer user_id, String password) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        if (isValid(password)) {
            user.setPassword(password);
            userRepository.save(user);
            return "ok";
        }
        return "invalid password";
    }

    // 3- Extra Endpoint 'refundPurchase' is used to return the money back to user after a valid refund
    public String refundPurchase(Integer user_id, Integer merchant_id, Integer product_id) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        Merchant merchant = findMerchantById(merchant_id);
        if (merchant == null) return "invalid merchant";

        Product product = findProductById(product_id);
        if (product == null) return "invalid product";

        for (Product p : orderHistory) {
            if (p.getId().equals(product_id)) {
                user.setBalance(user.getBalance() + product.getPrice());
                userRepository.save(user);
                return "ok";
            }
        }
        return "not found";
    }

    // 4- Extra Endpoint 'addToWishlist' allows the user to add a product by its ID to their wishlist
    public String addToWishlist(Integer user_id, Integer product_id) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        Product product = findProductById(product_id);
        if (product == null) return "invalid product";

        if (wishlist.contains(product)) {
            return "already wished";
        }

        wishlist.add(product);
        return "ok";
    }

    // 5- Extra Endpoint 'removeFromWishlist' allows the user to remove a product by its ID from their wishlist
    @Transactional
    public String removeFromWishlist(Integer user_id, Integer product_id) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        Product product = findProductById(product_id);
        if (product == null) return "invalid product";

        if (!wishlist.contains(product))
            return "not found";

        wishlist.remove(product);
        return "ok";
    }


    // 6- Extra Endpoint 'addToCart' allows the user to add a product to their cart
    @Transactional
    public String addToCart(Integer user_id, Integer product_id) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        Product product = findProductById(product_id);
        if (product == null) return "invalid product";

        if (cart.contains(product))
            return "already in cart";

        cart.add(product);
        return "ok";
    }


    // 7- Extra Endpoint 'removeFromCart' allows the user to remove a product from their cart
    public String removeFromCart(Integer user_id, Integer product_id) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        Product product = findProductById(product_id);
        if (product == null) return "invalid product";

        if (!cart.contains(product))
            return "not found";

        cart.remove(product);
        return "ok";
    }

    // 8- Extra Endpoint 'placeOrder' allows the user to buy the products in their cart
    public String placeOrder(Integer user_id) {
        User user = findUserById(user_id);
        if (user == null) return "invalid user";

        if (cart.isEmpty())
            return "empty";

        if (user.getBalance() >= getTotalPrice(user_id)) {
            for (MerchantStock ms : merchantStockRepository.findAll()) {
                for (int i = 0; i < cart.size(); i++) {
                    if (ms.getProduct_id().equals(cart.get(i).getId())) {
                        if (ms.getStock() > 0) {
                            ms.setStock(ms.getStock() - 1);
                            user.setBalance(user.getBalance() - cart.get(i).getPrice());
                            orderHistory.addAll(cart);
                            cart.remove(i);
                        }
                    }
                }
            }
        } else return "no balance";
        return "ok";
    }


    // Helper Methods:

    // A method to check validity of a password
    private Boolean isValid(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
    }

    // A method to find a user by ID
    private User findUserById(Integer user_id) {
        for (User u : userRepository.findAll())
            if (u.getId().equals(user_id))
                return u;

        return null;
    }

    // A method to find a product by ID
    private Product findProductById(Integer product_id) {
        for (Product p : productRepository.findAll())
            if (p.getId().equals(product_id))
                return p;
        return null;
    }

    // A method to find a merchant by ID
    private Merchant findMerchantById(Integer merchant_id) {
        for (Merchant m : merchantRepository.findAll())
            if (m.getId().equals(merchant_id))
                return m;

        return null;
    }

    // A method to get the total price in user's cart
    private Double getTotalPrice(Integer user_id) {
        double total = 0;
        for (Product p : cart)
            total += p.getPrice();

        return total;
    }
}

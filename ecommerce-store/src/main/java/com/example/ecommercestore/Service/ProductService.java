package com.example.ecommercestore.Service;

import com.example.ecommercestore.Model.Category;
import com.example.ecommercestore.Model.Product;
import com.example.ecommercestore.Repository.CategoryRepository;
import com.example.ecommercestore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        if (productRepository.findAll().isEmpty())
            return null;

        return productRepository.findAll();
    }

    public String addProduct(Product product) {
        if (categoryRepository.findAll().isEmpty())
            return "null";

        Category category = findCategoryById(product.getCategory_id());
        if (category == null)
            return "invalid category";

        if (category.getId().equals(product.getCategory_id())) {
            productRepository.save(product);
            return "ok";
        }
        return "null";
    }

    public String updateProduct(Integer id, Product product) {
        Product oldProduct = productRepository.findById(id).get();
        if (oldProduct == null)
            return "invalid id";

        Category category = findCategoryById(product.getCategory_id());
        if (category == null)
            return "invalid category";

        if (category.getId().equals(product.getCategory_id())) {
            oldProduct.setName(product.getName());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setCategory_id(product.getCategory_id());
            productRepository.save(oldProduct);
            return "ok";
        }
        return "invalid category";
    }

    public Boolean deleteProduct(Integer id) {
        Product product = productRepository.findById(id).get();
        if (product == null)
            return false;

        productRepository.delete(product);
        return true;
    }

    private Category findCategoryById(Integer id) {
        for (Category c : categoryRepository.findAll())
            if (c.getId().equals(id))
                return c;

        return null;
    }
}

package com.example.ecommercestore.Service;

import com.example.ecommercestore.Model.Merchant;
import com.example.ecommercestore.Model.MerchantStock;
import com.example.ecommercestore.Model.Product;
import com.example.ecommercestore.Repository.MerchantRepository;
import com.example.ecommercestore.Repository.MerchantStockRepository;
import com.example.ecommercestore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private final MerchantStockRepository merchantStockRepository;
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;

    public List<MerchantStock> getAllMerchantStock() {
        if (merchantStockRepository == null)
            return null;

        return merchantStockRepository.findAll();
    }

    public String addMerchantStock(MerchantStock merchantStock) {
        Merchant merchant = findMerchantById(merchantStock.getMerchant_id());
        if (merchant == null)
            return "invalid merchant";

        Product product = findProductById(merchantStock.getProduct_id());
        if (product == null)
            return "invalid product";

        merchantStockRepository.save(merchantStock);
        return "ok";
    }

    public String updateMerchantStock(Integer id, MerchantStock merchantStock) {
        MerchantStock oldMerchantStock = merchantStockRepository.findById(id).get();
        if (oldMerchantStock == null)
            return "invalid id";

        Merchant merchant = findMerchantById(id);
        if (merchant == null)
            return "invalid merchant";

        Product product = findProductById(merchantStock.getProduct_id());
        if (product == null)
            return "invalid product";

        oldMerchantStock.setMerchant_id(merchantStock.getMerchant_id());
        oldMerchantStock.setProduct_id(merchantStock.getProduct_id());
        oldMerchantStock.setStock(merchantStock.getStock());
        merchantStockRepository.save(oldMerchantStock);
        return "ok";
    }

    public Boolean deleteMerchantStock(Integer id) {
        MerchantStock merchantStock = merchantStockRepository.findById(id).get();
        if (merchantStock == null)
            return false;

        merchantStockRepository.delete(merchantStock);
        return true;
    }

    // 9- Extra Endpoint 'restockProduct' is used to restock a current MerchantStock by any amount
    public String restockProduct(Integer merchant_id, Integer product_id, Integer quantity) {
        for (MerchantStock ms : merchantStockRepository.findAll()) {
            if (ms.getMerchant_id().equals(merchant_id) && ms.getProduct_id().equals(product_id)) {
                if (quantity > 0) {
                    ms.setStock(ms.getStock() + quantity);
                    return "ok";
                } return "invalid amount";
            }
        } return "invalid stock";
    }

    // Helper methods
    private Merchant findMerchantById(Integer id) {
        for (Merchant m : merchantRepository.findAll())
            if (m.getId().equals(id))
                return m;

        return null;
    }

    private Product findProductById(Integer id) {
        for (Product p : productRepository.findAll())
            if (p.getId().equals(id))
                return p;

        return null;
    }
}

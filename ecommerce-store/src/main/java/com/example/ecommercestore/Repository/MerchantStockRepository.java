package com.example.ecommercestore.Repository;

import com.example.ecommercestore.Model.MerchantStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantStockRepository extends JpaRepository<MerchantStock, Integer> {
}

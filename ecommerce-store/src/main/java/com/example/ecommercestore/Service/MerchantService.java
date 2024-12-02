package com.example.ecommercestore.Service;

import com.example.ecommercestore.Model.Merchant;
import com.example.ecommercestore.Repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public List<Merchant> getAllMerchants() {
        if (merchantRepository.count() == 0)
            return null;

        return merchantRepository.findAll();
    }

    public void addMerchant(Merchant merchant) {
        merchantRepository.save(merchant);
    }

    public Boolean updateMerchant(Integer id, Merchant merchant) {
        Merchant oldMerchant = merchantRepository.findById(id).get();
        if (oldMerchant == null)
            return false;

        oldMerchant.setName(merchant.getName());
        merchantRepository.save(oldMerchant);
        return true;
    }

    public Boolean deleteMerchant(Integer id) {
        Merchant merchant = merchantRepository.findById(id).get();
        if (merchant == null)
            return false;

        merchantRepository.delete(merchant);
        return true;
    }
}

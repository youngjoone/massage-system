
package com.example.massagesystem.shop;

import com.example.massagesystem.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository; // UserRepository 주입

    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Optional<Shop> getShopById(Long id) {
        return shopRepository.findById(id);
    }

    public Shop createShop(Shop shop) {
        return shopRepository.save(shop);
    }

    public Shop updateShop(Long id, Shop shopDetails) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new RuntimeException("Shop not found"));
        shop.setName(shopDetails.getName());
        shop.setAddress(shopDetails.getAddress());
        shop.setPhoneNumber(shopDetails.getPhoneNumber());
        return shopRepository.save(shop);
    }

    public void deleteShop(Long id) {
        Shop shopToDelete = shopRepository.findById(id).orElseThrow(() -> new RuntimeException("Shop not found"));

        // 해당 가게에 연결된 사용자가 있는지 확인
        if (!userRepository.findByShop(shopToDelete).isEmpty()) {
            throw new IllegalStateException("Cannot delete shop: Users are still associated with this shop.");
        }

        shopRepository.delete(shopToDelete);
    }
}

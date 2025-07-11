
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
        return shopRepository.findAllByDelFlagFalse();
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
        if (shopToDelete.isDelFlag()) {
            // 이미 삭제된 가게라면, 예외를 발생시키지 않고 단순히 반환하거나 로그를 남길 수 있습니다.
            // 여기서는 이미 삭제된 가게라는 메시지를 포함한 예외를 발생시켜 프론트엔드에 알립니다.
            throw new RuntimeException("Shop with ID " + id + " is already deleted.");
        }

        // 해당 가게에 연결된 사용자가 있는지 확인 (논리적 삭제 시에도 유효성 검사 유지)
        if (!userRepository.findByShop(shopToDelete).isEmpty()) {
            throw new IllegalStateException("Cannot delete shop: Users are still associated with this shop.");
        }

        shopToDelete.setDelFlag(true);
        shopRepository.save(shopToDelete);
    }
}

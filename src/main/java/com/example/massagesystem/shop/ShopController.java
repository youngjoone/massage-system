
package com.example.massagesystem.shop;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping
    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // ADMIN만 특정 Shop 조회 가능
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        return shopService.getShopById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // ADMIN만 Shop 생성 가능
    public Shop createShop(@Valid @RequestBody Shop shop) {
        return shopService.createShop(shop);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // ADMIN만 Shop 수정 가능
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody Shop shopDetails) {
        return ResponseEntity.ok(shopService.updateShop(id, shopDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // ADMIN만 Shop 삭제 가능
    public ResponseEntity<String> deleteShop(@PathVariable Long id) {
        try {
            shopService.deleteShop(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 409 Conflict
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Shop not found
        }
    }

    @GetMapping("/admin/shops")
    @PreAuthorize("hasRole('ADMIN')") // ADMIN만 모든 Shop 목록 조회 가능
    public List<Shop> getAllShopsForAdmin() {
        return shopService.getAllShops();
    }
}

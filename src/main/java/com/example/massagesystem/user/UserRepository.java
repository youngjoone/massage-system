package com.example.massagesystem.user;

import com.example.massagesystem.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndDelFlagFalse(String username);
    List<User> findByShop(Shop shop);
    List<User> findAllByDelFlagFalse();
    Optional<User> findByIdAndDelFlagFalse(Long id);
}

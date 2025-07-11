package com.example.massagesystem.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findAllByDelFlagFalse();
    Optional<Shop> findByIdAndDelFlagFalse(Long id);
}
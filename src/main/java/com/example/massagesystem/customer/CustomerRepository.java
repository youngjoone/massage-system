package com.example.massagesystem.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByShopId(Long shopId);
    List<Customer> findAllByDelFlagFalse();
    Optional<Customer> findByIdAndDelFlagFalse(Long id);
}

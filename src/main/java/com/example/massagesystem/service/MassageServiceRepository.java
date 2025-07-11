package com.example.massagesystem.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MassageServiceRepository extends JpaRepository<MassageService, Long> {
    List<MassageService> findAllByDelFlagFalse();
    Optional<MassageService> findByIdAndDelFlagFalse(Long id);
}
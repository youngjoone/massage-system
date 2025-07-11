package com.example.massagesystem.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByDelFlagFalse();
    Optional<Booking> findByIdAndDelFlagFalse(Long id);
}
package com.example.massagesystem.booking;

import com.example.massagesystem.user.User;
import com.example.massagesystem.service.MassageService;
import com.example.massagesystem.shop.Shop; // Shop 임포트
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private MassageService service;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @Column(nullable = false)
    private String status; // e.g., PENDING, CONFIRMED, CANCELLED

    @ManyToOne // Booking과 Shop은 Many-to-One 관계
    @JoinColumn(name = "shop_id", nullable = false) // shop_id 컬럼으로 매핑
    private Shop shop;
}

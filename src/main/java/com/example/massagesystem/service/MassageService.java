package com.example.massagesystem.service;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.massagesystem.shop.Shop; // Shop 임포트

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MassageService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int durationMinutes;

    @ManyToOne // MassageService와 Shop은 Many-to-One 관계
    @JoinColumn(name = "shop_id", nullable = false) // shop_id 컬럼으로 매핑
    private Shop shop;
}

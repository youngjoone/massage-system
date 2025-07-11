package com.example.massagesystem.booking;

import com.example.massagesystem.common.BaseEntity;
import com.example.massagesystem.user.User;
import com.example.massagesystem.service.MassageService;
import com.example.massagesystem.shop.Shop; // Shop 임포트
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private MassageService service;

    @NotNull(message = "예약 시간은 필수 입력 값입니다.")
    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @NotBlank(message = "상태는 필수 입력 값입니다.")
    @Column(nullable = false)
    private String status; // e.g., PENDING, CONFIRMED, CANCELLED

    @ManyToOne // Booking과 Shop은 Many-to-One 관계
    @JoinColumn(name = "shop_id", nullable = false) // shop_id 컬럼으로 매핑
    private Shop shop;
}

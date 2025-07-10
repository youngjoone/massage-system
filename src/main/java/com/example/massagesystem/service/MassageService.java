package com.example.massagesystem.service;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "서비스 이름은 필수 입력 값입니다.")
    @Size(max = 100, message = "서비스 이름은 100자를 초과할 수 없습니다.")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    @Column(length = 500)
    private String description;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    @Column(nullable = false)
    private int price;

    @NotNull(message = "시간은 필수 입력 값입니다.")
    @Min(value = 1, message = "시간은 1분 이상이어야 합니다.")
    @Column(nullable = false)
    private int durationMinutes;

    @ManyToOne // MassageService와 Shop은 Many-to-One 관계
    @JoinColumn(name = "shop_id", nullable = false) // shop_id 컬럼으로 매핑
    private Shop shop;
}

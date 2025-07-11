package com.example.massagesystem.customer;

import com.example.massagesystem.shop.ShopResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private ShopResponseDto shop; // ShopResponseDto 참조
}

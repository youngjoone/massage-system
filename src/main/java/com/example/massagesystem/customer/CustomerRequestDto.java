package com.example.massagesystem.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDto {
    private String name;
    private String phoneNumber;
    private Long shopId;
}

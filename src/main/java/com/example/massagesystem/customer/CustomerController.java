package com.example.massagesystem.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers(@RequestParam(required = false) Long shopId) {
        System.out.println("CustomerController received shopId: " + shopId);
        return customerService.getAllCustomers(shopId);
    }
}

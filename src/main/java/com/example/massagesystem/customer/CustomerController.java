package com.example.massagesystem.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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

    @PostMapping
    public Customer createCustomer(@RequestBody CustomerRequestDto requestDto) {
        return customerService.createCustomer(requestDto);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDto requestDto) {
        return customerService.updateCustomer(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}

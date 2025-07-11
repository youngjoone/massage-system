package com.example.massagesystem.customer;

import com.example.massagesystem.shop.ShopResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerResponseDto> getAllCustomers(Long shopId) {
        System.out.println("CustomerService received shopId: " + shopId);
        List<Customer> customers;
        if (shopId != null) {
            customers = customerRepository.findByShopId(shopId);
        } else {
            customers = customerRepository.findAll();
        }

        return customers.stream()
                .map(customer -> {
                    ShopResponseDto shopDto = null;
                    if (customer.getShop() != null) {
                        shopDto = new ShopResponseDto(customer.getShop().getId(), customer.getShop().getName());
                    }
                    return new CustomerResponseDto(customer.getId(), customer.getName(), customer.getPhoneNumber(), shopDto);
                })
                .collect(Collectors.toList());
    }
}

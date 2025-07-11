package com.example.massagesystem.customer;

import com.example.massagesystem.shop.ShopResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerResponseDto> getAllCustomers(Long shopId) {
        System.out.println("CustomerService received shopId: " + shopId);
        List<Customer> customers;
        if (shopId != null) {
            customers = customerRepository.findByShopId(shopId).stream()
                                        .filter(customer -> !customer.isDelFlag())
                                        .collect(Collectors.toList());
        } else {
            customers = customerRepository.findAllByDelFlagFalse();
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

    public void deleteCustomer(Long id) {
        Customer customerToDelete = customerRepository.findByIdAndDelFlagFalse(id)
                .orElseThrow(() -> new RuntimeException("Customer not found or already deleted"));
        customerToDelete.setDelFlag(true);
        customerRepository.save(customerToDelete);
    }
}

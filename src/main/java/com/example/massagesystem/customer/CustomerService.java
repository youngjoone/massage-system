package com.example.massagesystem.customer;

import com.example.massagesystem.shop.Shop;
import com.example.massagesystem.shop.ShopRepository;

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
    @Autowired
    private ShopRepository shopRepository;

    public List<CustomerResponseDto> getAllCustomers(Long shopId ) {
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

    public Customer createCustomer(CustomerRequestDto requestDto) {
        Shop shop = shopRepository.findById(requestDto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        Customer customer = new Customer();
        customer.setName(requestDto.getName());
        customer.setPhoneNumber(requestDto.getPhoneNumber());
        customer.setShop(shop);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, CustomerRequestDto requestDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setName(requestDto.getName());
        customer.setPhoneNumber(requestDto.getPhoneNumber());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customerToDelete = customerRepository.findByIdAndDelFlagFalse(id)
                .orElseThrow(() -> new RuntimeException("Customer not found or already deleted"));
        customerToDelete.setDelFlag(true);
        customerRepository.save(customerToDelete);
    }
}

package com.apt.api.service;

import com.apt.api.dto.request.AuthRequest;
import com.apt.api.dto.response.AuthResponse;
import com.apt.api.dto.response.CustomerResponse;
import com.apt.api.model.Customer;
import com.apt.api.repository.CustomerRepository;
import com.apt.api.token.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Authenticates user and generates JWT token
    public AuthResponse login(AuthRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail()).orElse(null);

        if (customer == null || !customer.isActive()) {
            return null;
        }

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            return null;
        }

        String token = jwtService.generateToken(customer.getEmail());
        CustomerResponse customerResponse = mapToResponse(customer);

        return new AuthResponse(token, customerResponse);
    }

    // Gets customer by email
    public CustomerResponse getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::mapToResponse)
                .orElse(null);
    }

    // Maps Customer to CustomerResponse
    private CustomerResponse mapToResponse(Customer c) {
        CustomerResponse res = new CustomerResponse();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setEmail(c.getEmail());
        res.setPhone(c.getPhone());
        res.setActive(c.isActive());
        res.setCreatedBy(c.getCreatedBy());
        res.setCreatedAt(c.getCreatedAt());
        res.setUpdatedBy(c.getUpdatedBy());
        res.setUpdatedAt(c.getUpdatedAt());
        return res;
    }
}
package com.apt.api.service;

import com.apt.api.dto.request.AuthRequest;
import com.apt.api.dto.response.AuthResponse;
import com.apt.api.model.Customer;
import com.apt.api.repository.CustomerRepository;
import com.apt.api.token.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Customer customer;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setPassword(passwordEncoder.encode("password123"));
        customer.setActive(true);
    }

    @Test
    void login_Success() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(jwtService.generateToken(any())).thenReturn("mock-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        assertNotNull(response.getCustomer());
        assertEquals("Test User", response.getCustomer().getName());
    }

    @Test
    void login_InvalidPassword() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

        AuthResponse response = authService.login(request);

        assertNull(response);
    }

    @Test
    void login_UserNotFound() {
        AuthRequest request = new AuthRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password123");

        when(customerRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        AuthResponse response = authService.login(request);

        assertNull(response);
    }

    @Test
    void login_InactiveUser() {
        customer.setActive(false);
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

        AuthResponse response = authService.login(request);

        assertNull(response);
    }
}
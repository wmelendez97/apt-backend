package com.apt.api.controller;

import com.apt.api.dto.request.OrderRequest;
import com.apt.api.dto.response.OrderResponse;
import com.apt.api.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private OrderController orderController;

    private OrderResponse orderResponse;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setCustomerId(1L);
        orderResponse.setStatus("PENDING");
        orderResponse.setTotal(BigDecimal.valueOf(100.00));

        orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
    }

    @Test
    void getAll_Success() {
        List<OrderResponse> orders = Arrays.asList(orderResponse);
        when(orderService.findAll()).thenReturn(orders);

        ResponseEntity response = orderController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getById_Success() {
        when(orderService.findById(1L)).thenReturn(orderResponse);

        ResponseEntity response = orderController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getById_NotFound() {
        when(orderService.findById(999L)).thenReturn(null);

        ResponseEntity response = orderController.getById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void create_Success() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.create(orderRequest, "test@example.com")).thenReturn(orderResponse);

        ResponseEntity response = orderController.create(orderRequest, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void create_Failure() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.create(orderRequest, "test@example.com")).thenReturn(null);

        ResponseEntity response = orderController.create(orderRequest, httpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void cancel_Success() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.cancel(1L, "test@example.com")).thenReturn(orderResponse);

        ResponseEntity response = orderController.cancel(1L, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void cancel_NotFound() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.cancel(999L, "test@example.com")).thenReturn(null);

        ResponseEntity response = orderController.cancel(999L, httpRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void update_Success() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.update(1L, orderRequest, "test@example.com")).thenReturn(orderResponse);

        ResponseEntity response = orderController.update(1L, orderRequest, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update_NotFound() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.update(999L, orderRequest, "test@example.com")).thenReturn(null);

        ResponseEntity response = orderController.update(999L, orderRequest, httpRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void reactivate_Success() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.reactivate(1L, "test@example.com")).thenReturn(orderResponse);

        ResponseEntity response = orderController.reactivate(1L, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void reactivate_NotFound() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(orderService.reactivate(999L, "test@example.com")).thenReturn(null);

        ResponseEntity response = orderController.reactivate(999L, httpRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
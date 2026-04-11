package com.apt.api.service;

import com.apt.api.dto.request.OrderDetailRequest;
import com.apt.api.dto.request.OrderRequest;
import com.apt.api.dto.response.OrderResponse;
import com.apt.api.dto.response.ProductResponse;
import com.apt.api.model.Order;
import com.apt.api.model.OrderDetail;
import com.apt.api.proxy.contract.ProductApiClient;
import com.apt.api.repository.OrderDetailRepository;
import com.apt.api.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductApiClient productApiClient;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private ProductResponse product;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setStatus("PENDING");
        order.setTotal(BigDecimal.valueOf(100.00));

        product = new ProductResponse();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(50.0);
    }

    @Test
    void create_Success() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        OrderDetailRequest detail = new OrderDetailRequest();
        detail.setProductId(1L);
        detail.setQuantity(2);
        request.setDetails(Arrays.asList(detail));

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productApiClient.getProductById(1L)).thenReturn(product);
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(new OrderDetail());
        when(orderDetailRepository.findByOrderId(1L)).thenReturn(Arrays.asList());

        OrderResponse response = orderService.create(request, "test@example.com");

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void cancel_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderDetailRepository.findByOrderId(1L)).thenReturn(Arrays.asList());

        OrderResponse response = orderService.cancel(1L, "test@example.com");

        assertNotNull(response);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void cancel_NotPending() {
        order.setStatus("PAID");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.cancel(1L, "test@example.com");

        assertNull(response);
    }

    @Test
    void reactivate_Success() {
        order.setStatus("CANCELLED");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderDetailRepository.findByOrderId(1L)).thenReturn(Arrays.asList());

        OrderResponse response = orderService.reactivate(1L, "test@example.com");

        assertNotNull(response);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void reactivate_NotCancelled() {
        order.setStatus("PENDING");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.reactivate(1L, "test@example.com");

        assertNull(response);
    }

    @Test
    void update_Success() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        OrderDetailRequest detail = new OrderDetailRequest();
        detail.setProductId(1L);
        detail.setQuantity(3);
        request.setDetails(Arrays.asList(detail));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productApiClient.getProductById(1L)).thenReturn(product);
        when(orderDetailRepository.findByOrderId(1L)).thenReturn(Arrays.asList());
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(new OrderDetail());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.update(1L, request, "test@example.com");

        assertNotNull(response);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void update_NotPending() {
        order.setStatus("PAID");
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.update(1L, request, "test@example.com");

        assertNull(response);
    }
}
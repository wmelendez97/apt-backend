package com.apt.api.service;

import com.apt.api.dto.request.PaymentRequest;
import com.apt.api.dto.response.PaymentResponse;
import com.apt.api.model.Order;
import com.apt.api.model.Payment;
import com.apt.api.repository.OrderRepository;
import com.apt.api.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setStatus("PENDING");
        order.setTotal(BigDecimal.valueOf(100.00));

        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setStatus("APPROVED");
        payment.setPaymentMethod("CREDIT_CARD");
        payment.setAmount(BigDecimal.valueOf(100.00));
    }

    @Test
    void processPayment_Success() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CREDIT_CARD");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        PaymentResponse response = paymentService.processPayment(request, "test@example.com");

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
        assertEquals("CREDIT_CARD", response.getPaymentMethod());
        verify(orderRepository).save(any(Order.class));
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void processPayment_OrderNotFound() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(999L);
        request.setPaymentMethod("CREDIT_CARD");

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        PaymentResponse response = paymentService.processPayment(request, "test@example.com");

        assertNull(response);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void processPayment_OrderNotPending() {
        order.setStatus("PAID");
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CREDIT_CARD");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        PaymentResponse response = paymentService.processPayment(request, "test@example.com");

        assertNull(response);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void findByOrderId_Success() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.findByOrderId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void findByOrderId_NotFound() {
        when(paymentRepository.findByOrderId(999L)).thenReturn(Optional.empty());

        PaymentResponse response = paymentService.findByOrderId(999L);

        assertNull(response);
    }
}
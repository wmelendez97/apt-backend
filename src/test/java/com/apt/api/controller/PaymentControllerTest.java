package com.apt.api.controller;

import com.apt.api.dto.request.PaymentRequest;
import com.apt.api.dto.response.PaymentResponse;
import com.apt.api.service.PaymentService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(1L);
        paymentRequest.setPaymentMethod("CREDIT_CARD");

        paymentResponse = new PaymentResponse();
        paymentResponse.setId(1L);
        paymentResponse.setOrderId(1L);
        paymentResponse.setStatus("APPROVED");
        paymentResponse.setPaymentMethod("CREDIT_CARD");
        paymentResponse.setAmount(BigDecimal.valueOf(100.00));
    }

    @Test
    void processPayment_Success() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(paymentService.processPayment(paymentRequest, "test@example.com")).thenReturn(paymentResponse);

        ResponseEntity response = paymentController.processPayment(paymentRequest, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void processPayment_Failure() {
        when(httpRequest.getAttribute("authenticatedUser")).thenReturn("test@example.com");
        when(paymentService.processPayment(paymentRequest, "test@example.com")).thenReturn(null);

        ResponseEntity response = paymentController.processPayment(paymentRequest, httpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getByOrderId_Success() {
        when(paymentService.findByOrderId(1L)).thenReturn(paymentResponse);

        ResponseEntity response = paymentController.getByOrderId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getByOrderId_NotFound() {
        when(paymentService.findByOrderId(999L)).thenReturn(null);

        ResponseEntity response = paymentController.getByOrderId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
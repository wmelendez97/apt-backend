package com.apt.api.service;

import com.apt.api.dto.request.PaymentRequest;
import com.apt.api.dto.response.PaymentResponse;
import com.apt.api.model.Order;
import com.apt.api.model.Payment;
import com.apt.api.repository.OrderRepository;
import com.apt.api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // Processes payment for an order
    public PaymentResponse processPayment(PaymentRequest req) {
        Order order = orderRepository.findById(req.getOrderId()).orElse(null);
        if (order == null || !order.getStatus().equals("PENDING")) {
            return null;
        }

        Payment payment = new Payment();
        payment.setOrderId(req.getOrderId());
        payment.setPaymentMethod(req.getPaymentMethod());
        payment.setAmount(order.getTotal());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setStatus("APPROVED");
        payment.setCreatedBy("system");
        payment.setCreatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        order.setStatus("PAID");
        order.setUpdatedBy("system");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return mapToResponse(saved);
    }

    // Finds payment by order ID
    public PaymentResponse findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    // Maps Payment to PaymentResponse
    private PaymentResponse mapToResponse(Payment p) {
        PaymentResponse res = new PaymentResponse();
        res.setId(p.getId());
        res.setOrderId(p.getOrderId());
        res.setStatus(p.getStatus());
        res.setPaymentMethod(p.getPaymentMethod());
        res.setAmount(p.getAmount());
        res.setTransactionId(p.getTransactionId());
        res.setCreatedBy(p.getCreatedBy());
        res.setCreatedAt(p.getCreatedAt());
        return res;
    }
}
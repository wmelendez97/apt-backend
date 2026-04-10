package com.apt.api.controller;

import com.apt.api.dto.request.PaymentRequest;
import com.apt.api.dto.response.PaymentResponse;
import com.apt.api.service.PaymentService;
import com.apt.api.util.ApiError;
import com.apt.api.util.ApiMessages;
import com.apt.api.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing simulation")
public class PaymentController {

    private final PaymentService paymentService;

    // Processes payment for an order
    @PostMapping
    @Operation(summary = "Process payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request) {
        PaymentResponse data = paymentService.processPayment(request);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data, ApiMessages.SUCCESS_CREATION))
                : ResponseEntity.badRequest()
                .body(new ApiResponse<>(ApiMessages.ERROR_PROCESS,
                        List.of(ApiError.ErrorCodes.BAD_REQUEST), HttpStatus.BAD_REQUEST));
    }

    // Gets payment by order ID
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByOrderId(@PathVariable Long orderId) {
        PaymentResponse data = paymentService.findByOrderId(orderId);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND,
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }
}
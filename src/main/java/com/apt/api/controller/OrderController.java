package com.apt.api.controller;

import com.apt.api.dto.request.OrderRequest;
import com.apt.api.dto.response.OrderResponse;
import com.apt.api.service.OrderService;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management")
public class OrderController {

    private final OrderService orderService;

    // Returns all orders
    @GetMapping
    @Operation(summary = "List all orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(orderService.findAll()));
    }

    // Returns an order by its ID
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) {
        OrderResponse data = orderService.findById(id);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND,
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    // Creates a new order
    @PostMapping
    @Operation(summary = "Create order")
    public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody OrderRequest request) {
        OrderResponse data = orderService.create(request);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data, ApiMessages.SUCCESS_CREATION))
                : ResponseEntity.badRequest()
                .body(new ApiResponse<>(ApiMessages.ERROR_PROCESS,
                        List.of(ApiError.ErrorCodes.BAD_REQUEST), HttpStatus.BAD_REQUEST));
    }

    // Cancels an existing order
    @PostMapping("/cancel/{id}")
    @Operation(summary = "Cancel order")
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(@PathVariable Long id) {
        OrderResponse data = orderService.cancel(id);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND,
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    // Updates an existing order
    @PutMapping("/{id}")
    @Operation(summary = "Update order")
    public ResponseEntity<ApiResponse<OrderResponse>> update(@PathVariable Long id, @RequestBody OrderRequest request) {
        OrderResponse data = orderService.update(id, request);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data, ApiMessages.SUCCESS_UPDATE))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND,
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    // Reactivates a cancelled order
    @PostMapping("/reactivate/{id}")
    @Operation(summary = "Reactivate order")
    public ResponseEntity<ApiResponse<OrderResponse>> reactivate(@PathVariable Long id) {
        OrderResponse data = orderService.reactivate(id);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND,
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }
}
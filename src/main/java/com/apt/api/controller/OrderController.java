package com.apt.api.controller;

import com.apt.api.dto.request.OrderRequest;
import com.apt.api.dto.response.OrderResponse;
import com.apt.api.service.OrderService;
import com.apt.api.token.TokenRequired;
import com.apt.api.util.ApiError;
import com.apt.api.util.ApiMessages;
import com.apt.api.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management")
public class OrderController {

    private final OrderService orderService;

    // Returns all orders
    @GetMapping
    @TokenRequired
    @Operation(summary = "List all orders", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(orderService.findAll()));
    }

    // Returns an order by its ID
    @GetMapping("/{id}")
    @TokenRequired
    @Operation(summary = "Get order by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) {
        OrderResponse data = orderService.findById(id);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND.getMessage(),
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    // Creates a new order
    @PostMapping
    @TokenRequired
    @Operation(summary = "Create order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody OrderRequest request, HttpServletRequest httpRequest) {
        String email = (String) httpRequest.getAttribute("authenticatedUser");
        OrderResponse data = orderService.create(request, email);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data, ApiMessages.SUCCESS_CREATION.getMessage()))
                : ResponseEntity.badRequest()
                .body(new ApiResponse<>(ApiMessages.ERROR_PROCESS.getMessage(),
                        List.of(ApiError.ErrorCodes.BAD_REQUEST), HttpStatus.BAD_REQUEST));
    }

    // Cancels an existing order
    @PostMapping("/cancel/{id}")
    @TokenRequired
    @Operation(summary = "Cancel order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(@PathVariable Long id, HttpServletRequest httpRequest) {
        String email = (String) httpRequest.getAttribute("authenticatedUser");
        OrderResponse data = orderService.cancel(id, email);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND.getMessage(),
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    // Updates an existing order
    @PutMapping("/{id}")
    @TokenRequired
    @Operation(summary = "Update order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<OrderResponse>> update(@PathVariable Long id, @RequestBody OrderRequest request, HttpServletRequest httpRequest) {
        String email = (String) httpRequest.getAttribute("authenticatedUser");
        OrderResponse data = orderService.update(id, request, email);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data, ApiMessages.SUCCESS_UPDATE.getMessage()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND.getMessage(),
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    // Reactivates a cancelled order
    @PostMapping("/reactivate/{id}")
    @TokenRequired
    @Operation(summary = "Reactivate order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<OrderResponse>> reactivate(@PathVariable Long id, HttpServletRequest httpRequest) {
        String email = (String) httpRequest.getAttribute("authenticatedUser");
        OrderResponse data = orderService.reactivate(id, email);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND.getMessage(),
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }
}
package com.apt.api.controller;

import com.apt.api.dto.response.ProductResponse;
import com.apt.api.service.ProductService;
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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Proxy to external products API")
public class ProductController {

    private final ProductService productService;

    // Returns all available products
    @GetMapping
    @TokenRequired
    @Operation(summary = "List all products", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {
        try {
            List<ProductResponse> data = productService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse<>(data));
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body(new ApiResponse<>(ApiMessages.ERROR_EXTERNAL_SERVICE_TIMEOUT,
                            List.of(ApiError.ErrorCodes.EXTERNAL_SERVICE_TIMEOUT), HttpStatus.GATEWAY_TIMEOUT));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ApiResponse<>(ApiMessages.ERROR_EXTERNAL_SERVICE,
                            List.of(ApiError.ErrorCodes.EXTERNAL_SERVICE_ERROR), HttpStatus.BAD_GATEWAY));
        }
    }

    // Returns a product by its ID
    @GetMapping("/{id}")
    @TokenRequired
    @Operation(summary = "Get product by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        try {
            ProductResponse data = productService.getProductById(id);
            if (data == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND,
                                List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
            }
            return ResponseEntity.ok(new ApiResponse<>(data));
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body(new ApiResponse<>(ApiMessages.ERROR_EXTERNAL_SERVICE_TIMEOUT,
                            List.of(ApiError.ErrorCodes.EXTERNAL_SERVICE_TIMEOUT), HttpStatus.GATEWAY_TIMEOUT));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ApiResponse<>(ApiMessages.ERROR_EXTERNAL_SERVICE,
                            List.of(ApiError.ErrorCodes.EXTERNAL_SERVICE_ERROR), HttpStatus.BAD_GATEWAY));
        }
    }

    // Returns paginated products
    @GetMapping("/page")
    @TokenRequired
    @Operation(summary = "List paginated products", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<ProductResponse> data = productService.getProductsPaged(page, pageSize);
            return ResponseEntity.ok(new ApiResponse<>(data));
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body(new ApiResponse<>(ApiMessages.ERROR_EXTERNAL_SERVICE_TIMEOUT,
                            List.of(ApiError.ErrorCodes.EXTERNAL_SERVICE_TIMEOUT), HttpStatus.GATEWAY_TIMEOUT));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ApiResponse<>(ApiMessages.ERROR_EXTERNAL_SERVICE,
                            List.of(ApiError.ErrorCodes.EXTERNAL_SERVICE_ERROR), HttpStatus.BAD_GATEWAY));
        }
    }
}
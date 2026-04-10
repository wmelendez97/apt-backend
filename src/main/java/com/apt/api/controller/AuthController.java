package com.apt.api.controller;

import com.apt.api.dto.request.AuthRequest;
import com.apt.api.dto.response.AuthResponse;
import com.apt.api.dto.response.CustomerResponse;
import com.apt.api.service.AuthService;
import com.apt.api.token.TokenRequired;
import com.apt.api.util.ApiError;
import com.apt.api.util.ApiMessages;
import com.apt.api.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and authorization")
public class AuthController {

    private final AuthService authService;

    // Authenticates user and returns JWT token
    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        AuthResponse data = authService.login(request);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(ApiMessages.ERROR_UNAUTHORIZED,
                        List.of(ApiError.ErrorCodes.UNAUTHORIZED), HttpStatus.UNAUTHORIZED));
    }

    // Gets authenticated customer information
    @GetMapping("/me")
    @TokenRequired
    @Operation(summary = "Get authenticated user")
    public ResponseEntity<ApiResponse<CustomerResponse>> me(HttpServletRequest request) {
        String email = (String) request.getAttribute("authenticatedUser");
        CustomerResponse data = authService.getCustomerByEmail(email);
        return (data != null)
                ? ResponseEntity.ok(new ApiResponse<>(data))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ApiMessages.ERROR_NOT_FOUND,
                        List.of(ApiError.ErrorCodes.NOT_FOUND), HttpStatus.NOT_FOUND));
    }
}
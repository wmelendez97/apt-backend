package com.apt.api.dto.response;

public class AuthResponse {

    private String token;
    private CustomerResponse customer;

    public AuthResponse(String token, CustomerResponse customer) {
        this.token = token;
        this.customer = customer;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public CustomerResponse getCustomer() { return customer; }
    public void setCustomer(CustomerResponse customer) { this.customer = customer; }
}
package com.apt.api.dto.request;

import java.util.List;

public class OrderRequest {

    private Long customerId;
    private List<OrderDetailRequest> details;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public List<OrderDetailRequest> getDetails() { return details; }
    public void setDetails(List<OrderDetailRequest> details) { this.details = details; }
}
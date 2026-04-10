package com.apt.api.service;

import com.apt.api.dto.request.OrderDetailRequest;
import com.apt.api.dto.request.OrderRequest;
import com.apt.api.dto.response.OrderDetailResponse;
import com.apt.api.dto.response.OrderResponse;
import com.apt.api.dto.response.ProductResponse;
import com.apt.api.model.Order;
import com.apt.api.model.OrderDetail;
import com.apt.api.proxy.contract.ProductApiClient;
import com.apt.api.repository.OrderDetailRepository;
import com.apt.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductApiClient productApiClient;

    // Converts OrderDetail to OrderDetailResponse
    private OrderDetailResponse mapDetailToResponse(OrderDetail d) {
        OrderDetailResponse res = new OrderDetailResponse();
        res.setId(d.getId());
        res.setProductId(d.getProductId());
        res.setProductTitle(d.getProductTitle());
        res.setPrice(d.getPrice());
        res.setQuantity(d.getQuantity());
        res.setSubtotal(d.getSubtotal());
        res.setCreatedBy(d.getCreatedBy());
        res.setCreatedAt(d.getCreatedAt());
        res.setUpdatedBy(d.getUpdatedBy());
        res.setUpdatedAt(d.getUpdatedAt());
        return res;
    }

    // Converts Order to OrderResponse including its details
    private OrderResponse mapToResponse(Order o) {
        OrderResponse res = new OrderResponse();
        res.setId(o.getId());
        res.setCustomerId(o.getCustomerId());
        res.setStatus(o.getStatus());
        res.setTotal(o.getTotal());
        res.setCreatedBy(o.getCreatedBy());
        res.setCreatedAt(o.getCreatedAt());
        res.setUpdatedBy(o.getUpdatedBy());
        res.setUpdatedAt(o.getUpdatedAt());

        List<OrderDetailResponse> details = orderDetailRepository.findByOrderId(o.getId())
                .stream().map(this::mapDetailToResponse).collect(Collectors.toList());
        res.setDetails(details);
        return res;
    }

    // Lists all orders
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Finds an order by ID
    public OrderResponse findById(Long id) {
        return orderRepository.findById(id).map(this::mapToResponse).orElse(null);
    }

    // Creates a new order fetching prices from external API
    public OrderResponse create(OrderRequest dto) {
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setStatus("PENDING");
        order.setCreatedBy("system");
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderDetailRequest detailReq : dto.getDetails()) {
            ProductResponse product = productApiClient.getProductById(detailReq.getProductId());
            if (product == null) continue;

            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(detailReq.getQuantity()));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(saved);
            detail.setProductId(product.getId());
            detail.setProductTitle(product.getTitle());
            detail.setPrice(price);
            detail.setQuantity(detailReq.getQuantity());
            detail.setSubtotal(subtotal);
            detail.setCreatedBy("system");
            detail.setCreatedAt(LocalDateTime.now());

            orderDetailRepository.save(detail);
            total = total.add(subtotal);
        }

        saved.setTotal(total);
        orderRepository.save(saved);

        return mapToResponse(saved);
    }

    // Cancels an order if it is in PENDING status
    public OrderResponse cancel(Long id) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getStatus().equals("PENDING")) return null;
            order.setStatus("CANCELLED");
            order.setUpdatedBy("system");
            order.setUpdatedAt(LocalDateTime.now());
            return mapToResponse(orderRepository.save(order));
        }).orElse(null);
    }

    // Updates an existing order
    public OrderResponse update(Long id, OrderRequest dto) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getStatus().equals("PENDING")) return null;

            // Delete existing details
            orderDetailRepository.findByOrderId(id).forEach(orderDetailRepository::delete);

            BigDecimal total = BigDecimal.ZERO;

            for (OrderDetailRequest detailReq : dto.getDetails()) {
                ProductResponse product = productApiClient.getProductById(detailReq.getProductId());
                if (product == null) continue;

                BigDecimal price = BigDecimal.valueOf(product.getPrice());
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(detailReq.getQuantity()));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProductId(product.getId());
                detail.setProductTitle(product.getTitle());
                detail.setPrice(price);
                detail.setQuantity(detailReq.getQuantity());
                detail.setSubtotal(subtotal);
                detail.setCreatedBy(order.getCreatedBy());
                detail.setCreatedAt(LocalDateTime.now());

                orderDetailRepository.save(detail);
                total = total.add(subtotal);
            }

            order.setTotal(total);
            order.setUpdatedBy("system");
            order.setUpdatedAt(LocalDateTime.now());

            return mapToResponse(orderRepository.save(order));
        }).orElse(null);
    }

    // Reactivates a cancelled order to PENDING status
    public OrderResponse reactivate(Long id) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getStatus().equals("CANCELLED")) return null;
            order.setStatus("PENDING");
            order.setUpdatedBy("system");
            order.setUpdatedAt(LocalDateTime.now());
            return mapToResponse(orderRepository.save(order));
        }).orElse(null);
    }
}
package com.apt.api.proxy.contract;

import com.apt.api.dto.response.ProductResponse;

import java.util.List;

public interface ProductApiClient {

    // Returns list of products from external API
    List<ProductResponse> getAllProducts();

    // Returns product by ID from external API
    ProductResponse getProductById(Long id);

    // Fetches paginated products from external API
    List<ProductResponse> getProductsPaged(int page, int pageSize);
}
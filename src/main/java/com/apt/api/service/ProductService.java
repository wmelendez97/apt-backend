package com.apt.api.service;

import com.apt.api.dto.response.ProductResponse;
import com.apt.api.proxy.contract.ProductApiClient;
import com.apt.api.util.ApiError;
import com.apt.api.util.ApiMessages;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class ProductService {

    private final ProductApiClient dummyClient;
    private final ProductApiClient fakeStoreClient;

    @Value("${external.api.active:dummyjson}")
    private String activeApi;

    public ProductService(@Qualifier("dummyJsonClient") ProductApiClient dummyClient,
                          @Qualifier("fakeStoreClient") ProductApiClient fakeStoreClient) {
        this.dummyClient = dummyClient;
        this.fakeStoreClient = fakeStoreClient;
    }

    // Returns active client based on configuration
    private ProductApiClient getActiveClient() {
        return "fakestore".equals(activeApi) ? fakeStoreClient : dummyClient;
    }

    // Fetches all products from external client
    public List<ProductResponse> getAllProducts() {
        return getActiveClient().getAllProducts();
    }

    // Fetches a product by ID from external client
    public ProductResponse getProductById(Long id) {
        return getActiveClient().getProductById(id);
    }

    // Fetches paginated products and wraps them in a Page to maintain standard pattern
    public Page<ProductResponse> getProductsPaged(int page, int pageSize) {
        List<ProductResponse> data = getActiveClient().getProductsPaged(page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(data, pageable, data.size());
    }
}
package com.apt.api.proxy.client;

import com.apt.api.dto.external.DummyJsonListResponseDto;
import com.apt.api.dto.external.DummyJsonProductDto;
import com.apt.api.dto.response.ProductResponse;
import com.apt.api.proxy.adapter.DummyJsonProductAdapter;
import com.apt.api.proxy.contract.ProductApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyJsonClient implements ProductApiClient {

    private final RestTemplate restTemplate;
    private final DummyJsonProductAdapter adapter;

    // Fetches all products from dummyjson and maps them to ProductResponse
    @Override
    public List<ProductResponse> getAllProducts() {
        DummyJsonListResponseDto res = restTemplate.getForObject("/products?limit=0", DummyJsonListResponseDto.class);
        return adapter.toResponseList(res.getProducts());
    }

    // Fetches a product by ID from dummyjson and maps it to ProductResponse
    @Override
    public ProductResponse getProductById(Long id) {
        DummyJsonProductDto dto = restTemplate.getForObject("/products/{id}", DummyJsonProductDto.class, id);
        return adapter.toResponse(dto);
    }

    // Fetches paginated products from dummyjson delegating skip/limit to the external API
    @Override
    public List<ProductResponse> getProductsPaged(int page, int pageSize) {
        int skip = page * pageSize;
        String url = "/products?limit=" + pageSize + "&skip=" + skip;
        DummyJsonListResponseDto res = restTemplate.getForObject(url, DummyJsonListResponseDto.class);
        return adapter.toResponseList(res.getProducts());
    }
}
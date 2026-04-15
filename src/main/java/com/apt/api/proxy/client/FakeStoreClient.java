package com.apt.api.proxy.client;

import com.apt.api.dto.external.FakeStoreProductDto;
import com.apt.api.dto.response.ProductResponse;
import com.apt.api.proxy.adapter.FakeStoreProductAdapter;
import com.apt.api.proxy.contract.ProductApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component("fakeStoreClient")
@RequiredArgsConstructor
public class FakeStoreClient implements ProductApiClient {

    private final RestTemplate fakeStoreRestTemplate;
    private final FakeStoreProductAdapter adapter;

    // Fetches all products from FakeStore
    @Override
    public List<ProductResponse> getAllProducts() {
        FakeStoreProductDto[] arr = fakeStoreRestTemplate.getForObject("/products", FakeStoreProductDto[].class);
        return adapter.toResponseList(Arrays.asList(arr));
    }

    // Fetches product by ID from FakeStore
    @Override
    public ProductResponse getProductById(Long id) {
        FakeStoreProductDto dto = fakeStoreRestTemplate.getForObject("/products/{id}", FakeStoreProductDto.class, id);
        return adapter.toResponse(dto);
    }

    // Fetches paginated products from FakeStore
    @Override
    public List<ProductResponse> getProductsPaged(int page, int pageSize) {
        int lim = (page + 1) * pageSize;
        FakeStoreProductDto[] arr = fakeStoreRestTemplate.getForObject("/products?limit=" + lim, FakeStoreProductDto[].class);
        List<FakeStoreProductDto> lst = Arrays.asList(arr);
        int ski = page * pageSize;
        List<FakeStoreProductDto> pag = lst.subList(Math.min(ski, lst.size()), Math.min(ski + pageSize, lst.size()));
        return adapter.toResponseList(pag);
    }
}
package com.apt.api.proxy.adapter;

import com.apt.api.dto.external.FakeStoreProductDto;
import com.apt.api.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FakeStoreProductAdapter {

    // Maps FakeStoreProductDto to ProductResponse
    public ProductResponse toResponse(FakeStoreProductDto dto) {
        ProductResponse res = new ProductResponse();
        res.setId(dto.getId());
        res.setTitle(dto.getTitle());
        res.setDescription(dto.getDescription());
        res.setPrice(dto.getPrice());
        res.setCategory(dto.getCategory());
        res.setThumbnail(dto.getImage());
        res.setImages(List.of(dto.getImage()));
        if (dto.getRating() != null) {
            res.setRating(dto.getRating().getRate());
            res.setStock(dto.getRating().getCount());
        }
        return res;
    }

    // Maps list of FakeStoreProductDto to list of ProductResponse
    public List<ProductResponse> toResponseList(List<FakeStoreProductDto> dtos) {
        return dtos.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
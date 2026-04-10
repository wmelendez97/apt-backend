package com.apt.api.proxy.adapter;

import com.apt.api.dto.external.DummyJsonProductDto;
import com.apt.api.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DummyJsonProductAdapter {

    // Maps a DummyJsonProductDto to ProductResponse
    public ProductResponse toResponse(DummyJsonProductDto dto) {
        ProductResponse res = new ProductResponse();
        res.setId(dto.getId());
        res.setTitle(dto.getTitle());
        res.setDescription(dto.getDescription());
        res.setPrice(dto.getPrice());
        res.setDiscountPercentage(dto.getDiscountPercentage());
        res.setRating(dto.getRating());
        res.setStock(dto.getStock());
        res.setBrand(dto.getBrand());
        res.setCategory(dto.getCategory());
        res.setThumbnail(dto.getThumbnail());
        res.setImages(dto.getImages());
        return res;
    }

    // Maps a list of DummyJsonProductDto to list of ProductResponse
    public List<ProductResponse> toResponseList(List<DummyJsonProductDto> dtos) {
        return dtos.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
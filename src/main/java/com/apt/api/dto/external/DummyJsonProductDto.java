package com.apt.api.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DummyJsonProductDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Double discountPercentage;
    private Double rating;
    private Integer stock;
    private String brand;
    private String category;
    private String thumbnail;
    private List<String> images;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Double getDiscountPercentage() { return discountPercentage; }
    public Double getRating() { return rating; }
    public Integer getStock() { return stock; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public String getThumbnail() { return thumbnail; }
    public List<String> getImages() { return images; }
}
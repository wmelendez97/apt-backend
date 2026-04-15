package com.apt.api.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeStoreProductDto {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

    public static class Rating {
        private Double rate;
        private Integer count;

        public Double getRate() { return rate; }
        public Integer getCount() { return count; }
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImage() { return image; }
    public Rating getRating() { return rating; }
}
package com.apt.api.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DummyJsonListResponseDto {
    private List<DummyJsonProductDto> products;
    private Integer total;
    private Integer skip;
    private Integer limit;

    public List<DummyJsonProductDto> getProducts() { return products; }
    public Integer getTotal() { return total; }
    public Integer getSkip() { return skip; }
    public Integer getLimit() { return limit; }
}
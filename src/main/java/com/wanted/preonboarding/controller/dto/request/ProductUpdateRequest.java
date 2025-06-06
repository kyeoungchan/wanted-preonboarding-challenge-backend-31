package com.wanted.preonboarding.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductUpdateRequest {
    private String name;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private Long sellerId;
    private Long brandId;
    private String status;

    private ProductCreateRequest.ProductDetailDto detail;
    private ProductCreateRequest.ProductPriceDto price;
    private List<ProductCreateRequest.ProductCategoryDto> categories;
    private List<Long> tags;
}

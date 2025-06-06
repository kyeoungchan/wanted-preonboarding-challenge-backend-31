package com.wanted.preonboarding.service.dto;

import com.wanted.preonboarding.service.product.ProductDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MainPageDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class MainPage {
        private List<ProductDto.ProductSummary> newProducts;
        private List<ProductDto.ProductSummary> popularProducts;
        private List<FeaturedCategory> featuredCategories;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class FeaturedCategory {
        private Long id;
        private String name;
        private String slug;
        private String imageUrl;
        private Integer productCount;
    }
}

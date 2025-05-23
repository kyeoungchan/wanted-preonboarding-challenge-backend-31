package com.wanted.preonboarding.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CategoryDto {

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class Category {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private Integer level;
        private String imageUrl;
        @Builder.Default
        private List<Category> children = new ArrayList<>();
    }

    @Data
    @Builder
    public static class CategoryProducts {
        private Detail category;
        private List<ProductDto.ProductSummary> items;
        private PaginationDto.PaginationInfo pagination;
    }

    @Data
    @Builder
    public static class Detail {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private Integer level;
        private String imageUrl;
        private ParentCategory parent;
    }

    @Data
    @Builder
    public static class ParentCategory {
        private Long id;
        private String name;
        private String slug;
    }
}

package com.wanted.preonboarding.service.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductDto {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateRequest {
        private String name;
        private String slug;
        private String shortDescription;
        private String fullDescription;
        private String status;

        private Long sellerId;
        private Long brandId;

        private Detail detail;
        private Price price;
        @Builder.Default
        private List<ProductCategory> categories = new ArrayList<>();
        @Builder.Default
        private List<OptionGroup> optionGroups = new ArrayList<>();
        @Builder.Default
        private List<Image> images = new ArrayList<>();
        @Builder.Default
        private List<Long> tagIds = new ArrayList<>();
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product {
        private Long id;
        private String name;
        private String slug;
        private String shortDescription;
        private String fullDescription;
        private Seller seller;
        private Brand brand;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Detail detail;
        private Price price;
        private List<ProductCategory> categories;
        private List<OptionGroup> optionGroups;
        private List<Image> images;
        private List<Tag> tags;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Seller {
        private Long id;
        private String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Brand {
        private Long id;
        private String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Detail {
        private Double weight;
        private Map<String, Object> dimensions; // JSON: {"width": float, "height": float, "depth": float}
        private String materials;
        private String countryOfOrigin;
        private String warrantyInfo;
        private String careInstructions;
        private Map<String, Object> addtionalInfo; // JSON object for additional information
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Price {
        private Double basePrice;
        private Double salePrice;
        private Double costPrice;
        private String currency;
        private Double taxRate;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Category {
        private Long id;
        private String name;
        private String slug;
        private ParentCategory parent; // null을 안 주기 위해서 따로 ParentCategory class 생성
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ParentCategory {
        private Long id;
        private String name;
        private String slug;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OptionGroup {
        private Long id;
        private String name;
        private Integer displayOrder;
        private List<Option> options;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Option {
        private Long id;
        private String name;
        private Double additionalPrice;
        private String sku;
        private Integer stock;
        private Integer displayOrder;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Image {
        private Long id;
        private String url;
        private String altText;
        private Boolean isPrimary;
        private Integer displayOrder;
        private Long optionId;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Tag {
        private Long id;
        private String name;
        private String slug;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ProductCategory {
        private Long id;
        private Boolean isPrimary;
    }
}

package com.wanted.preonboarding.service.product.command;

import com.wanted.preonboarding.service.product.ProductDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class ProductCommand {

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateProduct {
        private String name;
        private String slug;
        private String shortDescription;
        private String fullDescription;
        private Long sellerId;
        private Long brandId;
        private String status; // ACTIVE, OUT_OF_STOCK, DELETED

        private ProductDto.Detail detail;
        private ProductDto.Price price;
        @Builder.Default
        private List<ProductDto.ProductCategory> categories = new ArrayList<>();
        @Builder.Default
        private List<ProductDto.OptionGroup> optionGroups = new ArrayList<>();
        @Builder.Default
        private List<ProductDto.Image> images = new ArrayList<>();
        @Builder.Default
        private List<Long> tagIds = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UpdateProduct {
        private Long productId;
        private String name;
        private String slug;
        private String shortDescription;
        private String fullDescription;
        private Long sellerId;
        private Long brandId;
        private String status;

        private ProductDto.Detail detail;
        private ProductDto.Price price;
        private List<ProductDto.ProductCategory> categories;
        private List<Long> tagIds;
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DeleteProduct {
        private Long productId;
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AddProductOption {
        Long productId;
        ProductDto.Option option;
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UpdateProductOption {
        Long productId;
        ProductDto.Option option;
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DeleteProductOption {
        Long productId;
        Long optionId;
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AddProductImage {
        Long productId;
        ProductDto.Image image;
    }
}

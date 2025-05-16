package com.wanted.preonboarding.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCreateRequest {
    private String name;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private String status; // ACTIVE, OUT_OF_STOCK, DELTED
    private Long sellerId;
    private Long brandId;

    private ProductDetailDto detail;
    private ProductPriceDto price;
    private List<ProductCategoryDto> categories = new ArrayList<>();
    private List<Long> tagIds = new ArrayList<>();
    private List<ProductOptionGroupDto> optionGroups = new ArrayList<>();
    private List<ProductImageDto> images = new ArrayList<>();

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductDetailDto {
        private Double weight;
        private Map<String, Object> dimensions; // 크기(JSON)
        private String materials; // 소재정보
        private String countryOfOrigin; // 원산지
        private String warrantyInfo; // 보증정보
        private String careInstructions; // 관리 지침
        private Map<String, Object> additionalInfo; // 추가정보(JSONB)
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductPriceDto {
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private BigDecimal costPrice;
        private String currency;
        private BigDecimal taxRate;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductCategoryDto {
        private Long categoryId;
        private Boolean isPrimary; // 주요 카테고리 여부
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductOptionGroupDto {
        private String name; // 옵션 그룹명 (예: "색상", "사이즈")
        private Integer displayOrder; // 표시 순서
        private List<ProductOptionDto> options = new ArrayList<>();
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductOptionDto {
        private String name; // 옵션명 (예: "빨강", "XL")
        private Double addtionalPrice; // 추가 가격
        private String sku; // 재고 관리 코드
        private Integer stock; // 재고 수량
        private Integer displayOrder; // 표시 순서
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductImageDto {
        private String url;
        private String altText; // 대체 텍스트
        private Boolean isPrimary; // 대표 이미지 여부
        private Integer displayOrder; // 표시 순서
        private Long optionId; // 연관된 옵션 ID (FK, nullable)
    }
}

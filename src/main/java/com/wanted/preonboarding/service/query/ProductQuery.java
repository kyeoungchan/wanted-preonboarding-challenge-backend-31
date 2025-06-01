package com.wanted.preonboarding.service.query;

import com.wanted.preonboarding.service.dto.PaginationDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

public class ProductQuery {

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetProduct {
        private Long productId;
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ListProducts {
        private String status;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private List<Long> category;
        private Long seller;
        private Long brand;
        private Boolean inStock;
        private List<Long> tag;
        private String search;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate createdFrom;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate createdTo;

        private PaginationDto.PaginationRequest pagination;
    }
}

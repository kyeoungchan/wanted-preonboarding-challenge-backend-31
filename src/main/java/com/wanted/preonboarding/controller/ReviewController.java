package com.wanted.preonboarding.controller;

import com.wanted.preonboarding.controller.dto.response.ApiResponse;
import com.wanted.preonboarding.controller.mapper.ReviewControllerMapper;
import com.wanted.preonboarding.service.ReviewService;
import com.wanted.preonboarding.service.dto.PaginationDto;
import com.wanted.preonboarding.service.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewControllerMapper mapper;

    @GetMapping("/products/{id}/reviews")
    public ResponseEntity<ApiResponse<?>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "created_at:desc") String sort,
            @RequestParam(required = false) Integer rating
    ) {
        PaginationDto.PaginationRequest paginationRequest = PaginationDto.PaginationRequest.builder()
                .page(page)
                .size(perPage)
                .sort(sort)
                .build();

        // 서비스 호출
        ReviewDto.ReviewPage reviewPage = reviewService.getProductReviews(productId, rating, paginationRequest);

        return ResponseEntity.ok(ApiResponse.success(reviewPage, "상품 리뷰를 성공적으로 조회했습니다."));
    }
}

package com.wanted.preonboarding.service;

import com.wanted.preonboarding.entity.Review;
import com.wanted.preonboarding.exception.ResourceNotFoundException;
import com.wanted.preonboarding.repository.ProductRepository;
import com.wanted.preonboarding.repository.ReviewRepository;
import com.wanted.preonboarding.repository.UserRepository;
import com.wanted.preonboarding.service.dto.PaginationDto;
import com.wanted.preonboarding.service.dto.ReviewDto;
import com.wanted.preonboarding.service.mapper.ReviewMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public ReviewDto.ReviewPage getProductReviews(
            Long productId,
            Integer rating,
            PaginationDto.PaginationRequest paginationRequest
    ) {
        // 상품 존재 확인
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // 모든 리뷰 조회 (summary 계산용)
        List<Review> allReviews = reviewRepository.findAllByProductId(productId);

        // 리뷰 요약 정보 계산
        ReviewDto.ReviewSummary summary = reviewMapper.toReviewSummary(allReviews);

        // 평점 필터 적용하여 페이지 조회
        Page<Review> reviewPage;
        if (rating != null) {
            reviewPage = reviewRepository.findByProductIdAndRating(productId, rating, paginationRequest.toPageable());
        } else {
            reviewPage = reviewRepository.findByProductId(productId, paginationRequest.toPageable());
        }

        // 페이지 리뷰를 DTO로 변환
        List<ReviewDto.Review> reviewResponses = reviewPage.getContent().stream()
                .map(reviewMapper::toReviewDto)
                .toList();

        // 페이징 정보 생성
        PaginationDto.PaginationInfo paginationInfo = PaginationDto.PaginationInfo.builder()
                .totalItems((int) reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .currentPage(reviewPage.getNumber() + 1)
                .perPage(reviewPage.getSize())
                .build();

        // API 스펙에 맞게 응답 생성
        return ReviewDto.ReviewPage.builder()
                .items(reviewResponses)
                .summary(summary)
                .pagination(paginationInfo)
                .build();
    }
}

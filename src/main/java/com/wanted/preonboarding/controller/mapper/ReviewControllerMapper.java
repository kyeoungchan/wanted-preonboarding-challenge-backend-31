package com.wanted.preonboarding.controller.mapper;

import com.wanted.preonboarding.controller.dto.request.ReviewCreateRequest;
import com.wanted.preonboarding.controller.dto.request.ReviewUpdateRequest;
import com.wanted.preonboarding.service.dto.ReviewDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewControllerMapper {
    public ReviewDto.CreateRequest toReviewDtoCreateRequest(ReviewCreateRequest request) {
        return ReviewDto.CreateRequest.builder()
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public ReviewDto.UpdateRequest toReviewDtoUpdateRequest(ReviewUpdateRequest request) {
        return ReviewDto.UpdateRequest.builder()
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}

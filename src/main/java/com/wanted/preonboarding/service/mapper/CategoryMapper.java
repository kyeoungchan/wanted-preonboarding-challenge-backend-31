package com.wanted.preonboarding.service.mapper;

import com.wanted.preonboarding.service.entity.Category;
import com.wanted.preonboarding.service.dto.CategoryDto;
import com.wanted.preonboarding.service.dto.PaginationDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto.Category toCategoryResponse(Category category) {
        return CategoryDto.Category.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .level(category.getLevel())
                .imageUrl(category.getImageUrl())
                .children(category.getChildren().stream()
                        .map(this::toCategoryResponse)
                        .toList())
                .build();
    }

    public CategoryDto.Detail toCategoryDetail(Category category) {
        return CategoryDto.Detail.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .level(category.getLevel())
                .imageUrl(category.getImageUrl())
                .parent(CategoryDto.ParentCategory.builder()
                        .id(category.getParent().getId())
                        .name(category.getParent().getName())
                        .slug(category.getParent().getSlug())
                        .build())
                .build();
    }

    public PaginationDto.PaginationInfo toPaginationInfo(Page<?> page) {
        return PaginationDto.PaginationInfo.builder()
                .totalItems((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .perPage(page.getSize())
                .build();
    }
}

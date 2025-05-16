package com.wanted.preonboarding.controller;

import com.wanted.preonboarding.controller.dto.response.ApiResponse;
import com.wanted.preonboarding.service.CategoryService;
import com.wanted.preonboarding.service.dto.CategoryDto;
import com.wanted.preonboarding.service.dto.PaginationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCategories(@RequestParam(required = false) Integer level) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        categoryService.getAllCategories(level),
                        "카테고리 목록을 성공적으로 조회했습니다."
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable Long id) {
        CategoryDto.Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        category,
                        "카테고리 정보를 성공적으로 조회했습니다."
                )
        );
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ApiResponse<?>> getCategoryProducts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "created_at:desc") String sort,
            @RequestParam(defaultValue = "true") Boolean includeSubcategories
    ) {
        PaginationDto.PaginationRequest paginationRequest = PaginationDto.PaginationRequest.builder()
                .page(page)
                .size(perPage)
                .sort(sort)
                .build();

        // 서비스 호출
        CategoryDto.CategoryProducts response =
                categoryService.getCategoryProducts(id, includeSubcategories, paginationRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "카테고리 상품 목록을 성공적으로 조회했습니다."
                )
        );
    }
}

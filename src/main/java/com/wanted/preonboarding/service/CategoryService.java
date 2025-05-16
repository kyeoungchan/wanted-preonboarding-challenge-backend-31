package com.wanted.preonboarding.service;

import com.wanted.preonboarding.entity.Category;
import com.wanted.preonboarding.entity.Product;
import com.wanted.preonboarding.exception.ResourceNotFoundException;
import com.wanted.preonboarding.repository.CategoryRepository;
import com.wanted.preonboarding.repository.ProductRepository;
import com.wanted.preonboarding.service.dto.CategoryDto;
import com.wanted.preonboarding.service.dto.PaginationDto;
import com.wanted.preonboarding.service.mapper.CategoryMapper;
import com.wanted.preonboarding.service.mapper.ProductMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<CategoryDto.Category> getAllCategories(Integer level) {
        if (level != null) {
            // 특정 레벨의 카테고리만 조회하되, 그 하위 계층 구조도 포함
            List<Category> categories = categoryRepository.findByLevel(level);

            // 모든 카테고리 조회해서 계층 구조를 만들기 위한 준비
            List<Category> allCategories = categoryRepository.findAll();

            // 자식 카테고리 맵 구성(부모 ID -> 자식 카테고리 리스트)
            Map<Long, List<Category>> childrenMap = new HashMap<>();
            for (Category category : allCategories) {
                if (category.getParent() != null) {
                    Long parentId = category.getParent().getId();
                    childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
                }
            }

            // 요청된 레벨의 카테고리에서 자식 계층 채우기
            return categories.stream()
                    .map(category -> buildCategoryTree(category, childrenMap))
                    .toList();
        }

        // 계층 구조로 전체 카테고리 조회
        return buildCategoryHierarchy();
    }

    /**
     * 전체 카테고리의 계층 구조를 구성
     */
    private List<CategoryDto.Category> buildCategoryHierarchy() {
        // 1. 모든 카테고리 조회
        List<Category> allCategories = categoryRepository.findAll();

        // 2. 카테고리 ID -> 카테고리 맵 구성
/*
        Map<Long, Category> categoryMap = allCategories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));
*/

        // 3. 카테고리 맵 구성(부모 ID -> 자식 카테고리 리스트)
        Map<Long, List<Category>> childrenMap = new HashMap<>();

        for (Category category : allCategories) {
            if (category.getParent() != null) {
                Long parentId = category.getParent().getId();
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
            }
        }

        // 4. 최상위 카테고리(level=1) 추가
        List<Category> rootCategories = allCategories.stream()
                .filter(category -> category.getLevel() == 1)
                .toList();

        // 5. 계층 구조 구성 및 반환
        return rootCategories.stream()
                .map(root -> buildCategoryTree(root, childrenMap))
                .toList();
    }

    /**
     * 재귀적으로 카테고리 트리 구성
     */
    private CategoryDto.Category buildCategoryTree(Category category, Map<Long, List<Category>> childrenMap) {
        // 1. 현재 카테고리를 DTO로 변환
        CategoryDto.Category responseDto = categoryMapper.toCategoryResponse(category);

        // 2. 자식 카테고리가 있는 경우 재귀적으로 추가
        List<Category> children = childrenMap.getOrDefault(category.getId(), new ArrayList<>());
        if (!children.isEmpty()) {
            List<CategoryDto.Category> childrenDto = children.stream()
                    .map(child -> buildCategoryTree(child, childrenMap))
                    .toList();
            responseDto.setChildren(childrenDto);
        }

        return responseDto;
    }

    @Transactional(readOnly = true)
    public CategoryDto.Category getCategoryById(Long categoryId) {
        // 카테고리 존재 확인
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        // 모든 카테고리 조회해서 계층 구조를 만들기 위한 준비
        List<Category> allCategories = categoryRepository.findAll();

        // 자식 카테고리 맵 구성 (부모 ID -> 자식 카테고리 리스트)
        Map<Long, List<Category>> childrenMap = new HashMap<>();
        for (Category cat : allCategories) {
            if (cat.getParent() != null) {
                Long parentId = cat.getParent().getId();
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(cat);
            }
        }

        // 요청된 카테고리의 계층 구조 구성
        return buildCategoryTree(category, childrenMap);
    }

    @Transactional(readOnly = true)
    public CategoryDto.CategoryProducts getCategoryProducts(
            Long categoryId,
            Boolean includeSubcategories,
            PaginationDto.PaginationRequest paginationRequest
    ) {
        // 카테고리 존재 확인
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        // 카테고리 정보 매핑
        CategoryDto.Detail categoryDetail = categoryMapper.toCategoryDetail(category);

        // 상품 조회
        Page<Product> productPage;
        if (Boolean.TRUE.equals(includeSubcategories)) {
            List<Long> categoryIds = collectSubcategoryIds(categoryId);
            productPage = productRepository.findByCategoriesIdIn(categoryIds, paginationRequest.toPageable());
        } else {
            // 현재 카테고리만 조회
            productPage = productRepository.findByCategoriesId(categoryId, paginationRequest.toPageable());
        }

        // 응답 DTO 생성
        return CategoryDto.CategoryProducts.builder()
                .category(categoryDetail)
                .items(productPage.getContent().stream().map(productMapper::toProductSummaryDto).toList())
                .pagination(categoryMapper.toPaginationInfo(productPage))
                .build();
    }

    /**
     * 주어진 카테고리와 모든 하위 카테고리 ID 목록 수집
     */
    private List<Long> collectSubcategoryIds(Long rootCategoryId) {
        List<Long> result = new ArrayList<>();
        result.add(rootCategoryId); // 루트 카테고리 포함

        // 전체 카테고리 조회
        List<Category> allCategories = categoryRepository.findAll();

        // 카테고리 ID -> 카테고리 매핑
/*
        Map<Long, Category> categoryMap = allCategories.stream()
                .collect(Collectors.toMap(Category::getId, c -> c));
*/

        // 부모 ID -> 카테고리 매핑
        Map<Long, List<Category>> childrenMap = new HashMap<>();
        for (Category category : allCategories) {
            if (category.getParent() != null) {
                Long parentId = category.getParent().getId();
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
            }
        }

        // 재귀적으로 하위 카테고리 ID 수정
        collectChildCategoryIds(rootCategoryId, childrenMap, result);
        return result;
    }

    private void collectChildCategoryIds(Long categoryId, Map<Long, List<Category>> childrenMap, List<Long> result) {
        List<Category> children = childrenMap.getOrDefault(categoryId, new ArrayList<>());
        for (Category child : children) {
            result.add(child.getId());
            collectChildCategoryIds(child.getId(), childrenMap, result);
        }
    }
}

package com.wanted.preonboarding.service.query;

import com.wanted.preonboarding.controller.dto.response.ProductListResponse;
import com.wanted.preonboarding.exception.ResourceNotFoundException;
import com.wanted.preonboarding.service.dto.PaginationDto;
import com.wanted.preonboarding.service.product.ProductDto;
import com.wanted.preonboarding.service.query.entity.ProductDocument;
import com.wanted.preonboarding.service.query.entity.ProductSearchDocument;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueryService implements ProductQueryHandler {

    private final ProductSearchOperations productSearchOperations;
    private final ProductDocumentOperations productDocumentOperations;
    private final ProductDocumentMapper productDocumentMapper;

    @Override
    public ProductDto.Product getProduct(ProductQuery.GetProduct query) {
        Long productId = query.getProductId();

        // MongoDB에서 상품 정보 조회(Aggregation 을 이용한 단일 쿼리로 조인)
        ProductDocument productDocument = productDocumentOperations.findProductDocumentWithReferences(productId);

        if (productDocument == null) {
            throw new ResourceNotFoundException("Product", productId);
        }

        // 조회된 상품 정보를 DTO 로 변환하여 반환
        return productDocumentMapper.toProductDto(productDocument);
    }

    @Override
    public ProductListResponse getProducts(ProductQuery.ListProducts query) {
        // 1. Elasticsearch 에서 조건에 맞는 상품 ID 목록 조회
        SearchHits<ProductSearchDocument> searchHits = productSearchOperations.searchProductsByConditions(query);

        // 검색 결과가 없는 경우 빈 응답 반환
        if (searchHits.getTotalHits() == 0 || searchHits.getSearchHits().isEmpty()) {
            return ProductListResponse.builder()
                    .items(Collections.emptyList())
                    .pagination(createEmptyPaginationInfo(query))
                    .build();
        }

        // 2. 검색 결과의 ID 목록 추출
        List<Long> productIds = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(ProductSearchDocument::getId)
                .toList();

        // 3. MongoDb 에서 상품 상세 정보를 한 번에 조회
        List<ProductDocument> productDocuments = productDocumentOperations.findProductDocumentsWithReferences(productIds);

        // 4. 상품 정보를 DTO 로 변환
        List<ProductDto.ProductSummary> productSummaries = productDocuments.stream()
                .map(productDocumentMapper::toProductSummaryDto)
                .toList();

        // 5. Elasticsearch 검색 결과의 순서대로 정렬 (ID -> 객체 매핑)
        Map<Long, ProductDto.ProductSummary> productMap = productSummaries.stream()
                .collect(Collectors.toMap(ProductDto.ProductSummary::getId, p -> p));

        List<ProductDto.ProductSummary> orderedProductSummaries = productIds.stream()
                .map(productMap::get)
                .filter(Objects::nonNull)
                .toList();

        // 6. 페이지네이션 정보 생성
        long totalElements = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalElements / query.getPagination().getSize());

        PaginationDto.PaginationInfo paginationInfo = PaginationDto.PaginationInfo.builder()
                .totalItems((int) totalElements)
                .totalPages(totalPages)
                .currentPage(query.getPagination().getPage())
                .perPage(query.getPagination().getSize())
                .build();

        // 7. 응답 생성
        return ProductListResponse.builder()
                .items(orderedProductSummaries)
                .pagination(paginationInfo)
                .build();
    }

    /**
     * 빈 페이지네이션 정보 생성
     */
    private PaginationDto.PaginationInfo createEmptyPaginationInfo(ProductQuery.ListProducts query) {
        return PaginationDto.PaginationInfo.builder()
                .totalItems(0)
                .totalPages(0)
                .currentPage(query.getPagination().getPage())
                .perPage(query.getPagination().getSize())
                .build();
    }
}

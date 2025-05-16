package com.wanted.preonboarding.service;

import com.wanted.preonboarding.controller.dto.response.ProductListResponse;
import com.wanted.preonboarding.entity.Brand;
import com.wanted.preonboarding.entity.Category;
import com.wanted.preonboarding.entity.Product;
import com.wanted.preonboarding.entity.ProductDetail;
import com.wanted.preonboarding.entity.ProductImage;
import com.wanted.preonboarding.entity.ProductOption;
import com.wanted.preonboarding.entity.ProductOptionGroup;
import com.wanted.preonboarding.entity.ProductPrice;
import com.wanted.preonboarding.entity.Seller;
import com.wanted.preonboarding.entity.Tag;
import com.wanted.preonboarding.exception.ResourceNotFoundException;
import com.wanted.preonboarding.repository.BrandRepository;
import com.wanted.preonboarding.repository.CategoryRepository;
import com.wanted.preonboarding.repository.ProductOptionRepository;
import com.wanted.preonboarding.repository.ProductRepository;
import com.wanted.preonboarding.repository.ProductSpecification;
import com.wanted.preonboarding.repository.SellerRepository;
import com.wanted.preonboarding.repository.TagRepository;
import com.wanted.preonboarding.service.dto.PaginationDto;
import com.wanted.preonboarding.service.dto.ProductDto;
import com.wanted.preonboarding.service.mapper.ProductMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductMapper productMapper;

    public ProductDto.Product createProduct(ProductDto.CreateRequest request) {

        // 1. 기본 Product 엔티티 생성
        Product product = productMapper.toProductEntity(request);

        // 2. 연관 엔티티 생성
        if (request.getSellerId() != null) {
            Seller seller = sellerRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller", request.getSellerId()));
            product.setSeller(seller);
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", request.getBrandId()));
            product.setBrand(brand);
        }

        // 3. 저장 및 ID 획득
        product = productRepository.save(product);

        // 4. 연관 관계 설정 및 저장
        // ProductDetail 생성 및 저장
        if (request.getDetail() != null) {
            ProductDetail detail = productMapper.toProductDetailEntity(request.getDetail(), product);
            product.setDetail(detail);
        }

        // ProductPrice 생성 및 저장
        if (request.getPrice() != null) {
            ProductPrice price = productMapper.toProductPriceEntity(request.getPrice(), product);
            product.setPrice(price);
        }

        // 카테고리 연결
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            List<Long> categoryIds = request.getCategories().stream()
                    .map(ProductDto.ProductCategory::getId)
                    .toList();
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            product.getCategories().addAll(categories);
        }

        // 태그 연결
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            product.getTags().addAll(tags);
        }

        // 옵션 그룹 및 옵션 생성
        if (request.getOptionGroups() != null) {
            for (ProductDto.OptionGroup groupDto : request.getOptionGroups()) {
                ProductOptionGroup group = productMapper.toProductOptionGroupEntity(groupDto, product);
                product.getOptionGroups().add(group);

                // 옵션 생성
                if (groupDto.getOptions() != null) {
                    for (ProductDto.Option optionDto : groupDto.getOptions()) {
                        ProductOption option = productMapper.toProductOptionEntity(optionDto, group);
                        group.getOptions().add(option);
                    }
                }
            }
        }

        // 이미지 생성
        if (request.getImages() != null) {
            for (ProductDto.Image imageDto : request.getImages()) {
                ProductOption option = null;
                if (imageDto.getOptionId() != null) {
                    option = optionRepository.findById(imageDto.getOptionId())
                            .orElse(null);
                }
                ProductImage image = productMapper.toProductImageEntity(imageDto, product, option);
                product.getImages().add(image);
            }
        }

        // 최종 저장 및 응답 생성
        product = productRepository.save(product);
        return productMapper.toProductDto(product);
    }

    @Transactional(readOnly = true)
    public ProductListResponse getProducts(ProductDto.ListRequest request) {
        // Specification 생성 및 조합
        Specification<Product> spec = Specification.where(null);

        // 상태 필터
        if (request.getStatus() != null) {
            spec = spec.and(ProductSpecification.withStatus(request.getStatus()));
        }

        // 가격 범위 필터
        if (request.getMinPrice() != null) {
            spec = spec.and(ProductSpecification.withMinPrice(request.getMinPrice()));
        }

        if (request.getMaxPrice() != null) {
            spec = spec.and(ProductSpecification.withMaxPrice(request.getMaxPrice()));
        }

        // 카테고리 필터
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            spec = spec.and(ProductSpecification.withCategoryId(request.getCategory()));
        }

        // 판매자 필터
        if (request.getSeller() != null) {
            spec = spec.and(ProductSpecification.withSellerId(request.getSeller()));
        }

        // 브랜드 필터
        if (request.getBrand() != null) {
            spec = spec.and(ProductSpecification.withBrandId(request.getBrand()));
        }

        // 태그 필터
        if (request.getTag() != null && !request.getTag().isEmpty()) {
            spec = spec.and(ProductSpecification.withTagIds(request.getTag()));
        }

        // 재고 여부 필터
        if (request.getInStock() != null) {
            spec = spec.and(ProductSpecification.inStock(request.getInStock()));
        }

        // 검색어 필터
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            spec = spec.and(ProductSpecification.withSearch(request.getSearch()));
        }

        // 등록일 범위 필터
        if (request.getCreatedFrom() != null) {
            LocalDateTime fromDate = request.getCreatedFrom().atStartOfDay();
            spec = spec.and(ProductSpecification.withCreatedDateAfter(fromDate));
        }

        if (request.getCreatedTo() != null) {
            // 날짜의 끝(23:59:59)으로 설정
            LocalDateTime toDate = request.getCreatedTo().plusDays(1).atStartOfDay().minusSeconds(1);
            spec = spec.and(ProductSpecification.withCreatedDateBefore(toDate));
        }

        // 조회 실행
        Page<Product> productPage = productRepository.findAll(spec, request.getPagination().toPageable());

        // 결과 변환
        List<ProductDto.ProductSummary> productSummaries = productPage.stream()
                .map(productMapper::toProductSummaryDto)
                .toList();

        // 페이지네이션 정보 생성
        PaginationDto.PaginationInfo paginationInfo = PaginationDto.PaginationInfo.builder()
                .totalItems((int) productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .currentPage(request.getPagination().getPage())
                .perPage(request.getPagination().getSize())
                .build();

        // 응답 생성
        return ProductListResponse.builder()
                .items(productSummaries)
                .pagination(paginationInfo)
                .build();
    }
}

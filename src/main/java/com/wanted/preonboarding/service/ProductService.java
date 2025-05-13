package com.wanted.preonboarding.service;

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
import com.wanted.preonboarding.repository.SellerRepository;
import com.wanted.preonboarding.repository.TagRepository;
import com.wanted.preonboarding.service.dto.ProductDto;
import com.wanted.preonboarding.service.mapper.ProductMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        return productMapper.
    }
}

package com.wanted.preonboarding.service;

import com.wanted.preonboarding.service.entity.ProductStatus;
import com.wanted.preonboarding.controller.dto.response.ProductListResponse;
import com.wanted.preonboarding.service.entity.Brand;
import com.wanted.preonboarding.service.entity.Category;
import com.wanted.preonboarding.service.entity.Product;
import com.wanted.preonboarding.service.entity.ProductDetail;
import com.wanted.preonboarding.service.entity.ProductImage;
import com.wanted.preonboarding.service.entity.ProductOption;
import com.wanted.preonboarding.service.entity.ProductOptionGroup;
import com.wanted.preonboarding.service.entity.ProductPrice;
import com.wanted.preonboarding.service.entity.Seller;
import com.wanted.preonboarding.service.entity.Tag;
import com.wanted.preonboarding.exception.ResourceNotFoundException;
import com.wanted.preonboarding.service.repository.BrandRepository;
import com.wanted.preonboarding.service.repository.CategoryRepository;
import com.wanted.preonboarding.service.repository.ProductImageRepository;
import com.wanted.preonboarding.service.repository.ProductOptionGroupRepository;
import com.wanted.preonboarding.service.repository.ProductOptionRepository;
import com.wanted.preonboarding.service.repository.ProductRepository;
import com.wanted.preonboarding.service.repository.ProductSpecification;
import com.wanted.preonboarding.service.repository.SellerRepository;
import com.wanted.preonboarding.service.repository.TagRepository;
import com.wanted.preonboarding.service.dto.PaginationDto;
import com.wanted.preonboarding.service.product.ProductDto;
import com.wanted.preonboarding.service.mapper.ProductMapper;
import com.wanted.preonboarding.service.product.ProductCommand;
import com.wanted.preonboarding.service.product.ProductCommandHandler;
import com.wanted.preonboarding.service.query.ProductQuery;
import com.wanted.preonboarding.service.query.ProductQueryHandler;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements ProductCommandHandler {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductOptionGroupRepository optionGroupRepository;
    private final ProductImageRepository imageRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto.Product createProduct(ProductCommand.CreateProduct command) {

        // 1. 기본 Product 엔티티 생성
        Product product = productMapper.toProductEntity(command);

        // 2. 연관 엔티티 생성
        if (command.getSellerId() != null) {
            Seller seller = sellerRepository.findById(command.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller", command.getSellerId()));
            product.setSeller(seller);
        }

        if (command.getBrandId() != null) {
            Brand brand = brandRepository.findById(command.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", command.getBrandId()));
            product.setBrand(brand);
        }

        // 3. 저장 및 ID 획득
        product = productRepository.save(product);

        // 4. 연관 관계 설정 및 저장
        // ProductDetail 생성 및 저장
        if (command.getDetail() != null) {
            ProductDetail detail = productMapper.toProductDetailEntity(command.getDetail(), product);
            product.setDetail(detail);
        }

        // ProductPrice 생성 및 저장
        if (command.getPrice() != null) {
            ProductPrice price = productMapper.toProductPriceEntity(command.getPrice(), product);
            product.setPrice(price);
        }

        // 카테고리 연결
        if (command.getCategories() != null && !command.getCategories().isEmpty()) {
            List<Long> categoryIds = command.getCategories().stream()
                    .map(ProductDto.ProductCategory::getId)
                    .toList();
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            product.getCategories().addAll(categories);
        }

        // 태그 연결
        if (command.getTagIds() != null && !command.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(command.getTagIds());
            product.getTags().addAll(tags);
        }

        // 옵션 그룹 및 옵션 생성
        if (command.getOptionGroups() != null) {
            for (ProductDto.OptionGroup groupDto : command.getOptionGroups()) {
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
        if (command.getImages() != null) {
            for (ProductDto.Image imageDto : command.getImages()) {
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

    @Override
    @Transactional
    public ProductDto.Product updateProduct(ProductCommand.UpdateProduct command) {
        Product product = productRepository.findById(command.getProductId())
                .map(entity -> productMapper.updateProductEntity(command, entity))
                .orElseThrow(() -> new ResourceNotFoundException("Product", command.getProductId()));

        // 연관 엔티티 업데이트
        if (command.getSellerId() != null) {
            Seller seller = sellerRepository.findById(command.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller", command.getSellerId()));
            product.setSeller(seller);
        }

        if (command.getBrandId() != null) {
            Brand brand = brandRepository.findById(command.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", command.getBrandId()));
            product.setBrand(brand);
        }

        // ProductDetail 업데이트
        if (command.getDetail() != null && product.getDetail() != null) {
            productMapper.updateProductDetailEntity(command.getDetail(), product.getDetail());
        }

        // ProductPrice 업데이트
        if (command.getPrice() != null && product.getPrice() != null) {
            productMapper.updateProductPriceEntity(command.getPrice(), product.getPrice());
        }

        // 카테고리 업데이트
        if (command.getCategories() != null) {
            product.getCategories().clear();
            List<Long> categoryIds = command.getCategories().stream()
                    .map(ProductDto.ProductCategory::getId)
                    .toList();
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            product.getCategories().addAll(categories);
        }

        // 태그 업데이트
        if (command.getTagIds() != null) {
            product.getTags().clear();
            List<Tag> tags = tagRepository.findAllById(command.getTagIds());
            product.getTags().addAll(tags);
        }

        // 저장 및 응답 생성
        product = productRepository.save(product);

        return productMapper.toProductDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(ProductCommand.DeleteProduct command) {
        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", command.getProductId()));

        // 소프트 삭제
        product.setStatus(ProductStatus.DELETED);
        productRepository.save(product);

        // 하드 삭제가 필요한 경우 사용
//        productRepository.delete(product);
    }

    @Override
    @Transactional
    public ProductDto.Option addProductOption(ProductCommand.AddProductOption command) {
        Long productId = command.getProductId();
        ProductDto.Option request = command.getOption();
        Long optionGroupId = request.getOptionGroupId();

        ProductOptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("OptionGroup", optionGroupId));

        // 해당 옵션 그룹이 요청된 상품에 속하는지 확인
        if (!optionGroup.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("OptionGroup does not belong to the specified product");
        }

        // OptionDto
        ProductDto.Option optionDto = ProductDto.Option.builder()
                .optionGroupId(optionGroupId)
                .name(request.getName())
                .additionalPrice(request.getAdditionalPrice())
                .sku(request.getSku())
                .stock(request.getStock())
                .displayOrder(request.getDisplayOrder())
                .build();

        // 옵션 엔티티 생성 및 저장
        ProductOption option = productMapper.toProductOptionEntity(optionDto, optionGroup);
        option = optionRepository.save(option);

        return productMapper.toOptionDto(option);
    }

    @Override
    @Transactional
    public ProductDto.Option updateProductOption(ProductCommand.UpdateProductOption command) {
        Long productId = command.getProductId();
        ProductDto.Option request = command.getOption();
        Long optionId = request.getId();

        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("Option", optionId));

        // 해당 옵션이 요청된 상품에 속하는지 확인
        if (!option.getOptionGroup().getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Option does not belong to the specified product");
        }

        // 옵션 업데이트
        if (request.getName() != null) {
            option.setName(request.getName());
        }
        if (request.getAdditionalPrice() != null) {
            option.setAdditionalPrice(request.getAdditionalPrice());
        }

        if (request.getSku() != null) {
            option.setSku(request.getSku());
        }

        if (request.getStock() != null) {
            option.setStock(request.getStock());
        }

        if (request.getDisplayOrder() != null) {
            option.setDisplayOrder(request.getDisplayOrder());
        }

        option = optionRepository.save(option);
        return productMapper.toOptionDto(option);
    }

    @Override
    @Transactional
    public void deleteProductOption(ProductCommand.DeleteProductOption command) {
        Long productId = command.getProductId();
        Long optionId = command.getOptionId();

        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("Option", optionId));

        // 해당 옵션이 요청된 상품에 속하는지 확인
        if (!option.getOptionGroup().getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Option does not belong to the specified product");
        }

        optionRepository.delete(option);
    }

    @Override
    @Transactional
    public ProductDto.Image addProductImage(ProductCommand.AddProductImage command) {
        Long productId = command.getProductId();
        ProductDto.Image request = command.getImage();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        ProductOption option = null;
        if (request.getOptionId() != null) {
            option = optionRepository.findById(request.getOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Option", request.getOptionId()));

            // 해당 옵션이 요청된 상품에 속하는지 확인
            if (!option.getOptionGroup().getProduct().getId().equals(productId)) {
                throw new IllegalArgumentException("Option does not belong to the specified product");
            }
        }

        // 이미지 엔티티 생성 및 저장
        ProductImage image = productMapper.toProductImageEntity(request, product, option);
        image = imageRepository.save(image);

        return productMapper.toImageDto(image);
    }
}

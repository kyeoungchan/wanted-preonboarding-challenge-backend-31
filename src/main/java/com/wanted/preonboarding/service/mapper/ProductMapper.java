package com.wanted.preonboarding.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.constant.Status;
import com.wanted.preonboarding.entity.Product;
import com.wanted.preonboarding.entity.ProductDetail;
import com.wanted.preonboarding.entity.ProductImage;
import com.wanted.preonboarding.entity.ProductOption;
import com.wanted.preonboarding.entity.ProductOptionGroup;
import com.wanted.preonboarding.entity.ProductPrice;
import com.wanted.preonboarding.service.dto.ProductDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ObjectMapper mapper;

    public Product toProductEntity(ProductDto.CreateRequest request) {
        return Product.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .shortDescription(request.getShortDescription())
                .fullDescription(request.getFullDescription())
                .status(Status.valueOf(request.getStatus()))
                .build();
    }

    public ProductDetail toProductDetailEntity(ProductDto.Detail detail, Product product) {
        return ProductDetail.builder()
                .product(product)
                .weight(detail.getWeight())
                .dimensions(convertMapToJsonString(detail.getDimensions()))
                .materials(detail.getMaterials())
                .countryOfOrigin(detail.getCountryOfOrigin())
                .warrantyInfo(detail.getWarrantyInfo())
                .careInstructions(detail.getCareInstructions())
                .additionalInfo(convertMapToJsonString(detail.getAddtionalInfo()))
                .build();
    }

    public ProductPrice toProductPriceEntity(ProductDto.Price price, Product product) {
        return ProductPrice.builder()
                .product(product)
                .basePrice(price.getBasePrice())
                .salePrice(price.getSalePrice())
                .currency(price.getCurrency())
                .taxRate(price.getTaxRate())
                .build();
    }

    public ProductOptionGroup toProductOptionGroupEntity(ProductDto.OptionGroup group, Product product) {
        return ProductOptionGroup.builder()
                .product(product)
                .name(group.getName())
                .displayOrder(group.getDisplayOrder())
                .build();
    }

    public ProductOption toProductOptionEntity(ProductDto.Option option, ProductOptionGroup optionGroup) {
        return ProductOption.builder()
                .optionGroup(optionGroup)
                .name(option.getName())
                .additionalPrice(option.getAdditionalPrice())
                .sku(option.getSku())
                .stock(option.getStock())
                .displayOrder(option.getDisplayOrder())
                .build();
    }

    public ProductImage toProductImageEntity(ProductDto.Image image, Product product, ProductOption option) {
        return ProductImage.builder()
                .product(product)
                .url(image.getUrl())
                .altText(image.getAltText())
                .isPrimary(image.getIsPrimary())
                .displayOrder(image.getDisplayOrder())
                .option(option)
                .build();
    }

    @SneakyThrows
    private String convertMapToJsonString(Map<String, Object> map) {
        if (map == null) return null;

        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON String to Map", e);
        }
    }

    public ProductDto.Product toProductDto(Product product) {
        return ProductDto.Product.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .shortDescription(product.getShortDescription())
                .fullDescription(product.getFullDescription())
                .seller(toSellerDto(product.getSeller()))
                .brand(toBrandDto(product.getBrand()))
                .status(product.getStatus().name())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .detail(toDetailDto(product.getDetail()))
                .price(toPriceDto(product.getPrice()))
                .categories(product.getCategories().stream()
                        .map(this::toCategoryDto)
                        .collect(Collectors.toList())
                )
                .optionGroups(product.getOptionGroups().stream()
                        .map(this::toOptionGroupDto)
                        .collect(Collectors.toList())
                )
                .images(product.getImages().stream()
                        .map(this::toImageDto)
                        .collect(Collectors.toList())
                )
                .tags(product.getTags().stream()
                        .map(this::toTagDto)
                        .collect(Collectors.toList())
                )
                .build();
    }
}

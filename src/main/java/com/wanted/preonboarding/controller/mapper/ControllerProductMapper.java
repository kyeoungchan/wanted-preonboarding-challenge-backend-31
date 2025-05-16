package com.wanted.preonboarding.controller.mapper;

import com.wanted.preonboarding.controller.dto.request.ProductCreateRequest;
import com.wanted.preonboarding.controller.dto.request.ProductListRequest;
import com.wanted.preonboarding.service.dto.PaginationDto;
import com.wanted.preonboarding.service.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ControllerProductMapper {

    // controller dto -> service dto
    public ProductDto.CreateRequest toProductDtoCreateRequest(ProductCreateRequest request) {
        return ProductDto.CreateRequest.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .shortDescription(request.getShortDescription())
                .fullDescription(request.getFullDescription())
                .status(request.getStatus())
                .detail(toProductDtoDetail(request.getDetail()))
                .price(toProductDtoPrice(request.getPrice()))
                .categories(request.getCategories().stream().map(this::toProductDtoProductCategory).toList())
                .optionGroups(request.getOptionGroups().stream().map(this::toProductDtoOptionGroup).toList())
                .images(request.getImages().stream().map(this::toProductDtoImage).toList())

                .build();
    }

    public ProductDto.Detail toProductDtoDetail(ProductCreateRequest.ProductDetailDto request) {
        return ProductDto.Detail.builder()
                .weight(request.getWeight())
                .dimensions(request.getDimensions())
                .materials(request.getMaterials())
                .countryOfOrigin(request.getCountryOfOrigin())
                .warrantyInfo(request.getWarrantyInfo())
                .careInstructions(request.getCareInstructions())
                .additionalInfo(request.getAdditionalInfo())
                .build();
    }

    public ProductDto.Price toProductDtoPrice(ProductCreateRequest.ProductPriceDto request) {
        return ProductDto.Price.builder()
                .basePrice(request.getBasePrice())
                .salePrice(request.getSalePrice())
                .costPrice(request.getCostPrice())
                .currency(request.getCurrency())
                .taxRate(request.getTaxRate())
                .build();
    }

    public ProductDto.ProductCategory toProductDtoProductCategory(ProductCreateRequest.ProductCategoryDto request) {
        return ProductDto.ProductCategory.builder()
                .id(request.getCategoryId())
                .isPrimary(request.getIsPrimary())
                .build();
    }

    public ProductDto.OptionGroup toProductDtoOptionGroup(ProductCreateRequest.ProductOptionGroupDto request) {
        return ProductDto.OptionGroup.builder()
                .name(request.getName())
                .options(request.getOptions().stream().map(this::toProductDtoOption).toList())
                .displayOrder(request.getDisplayOrder())
                .build();
    }

    public ProductDto.Option toProductDtoOption(ProductCreateRequest.ProductOptionDto request) {
        return ProductDto.Option.builder()
                .name(request.getName())
                .additionalPrice(request.getAddtionalPrice())
                .sku(request.getSku())
                .stock(request.getStock())
                .displayOrder(request.getDisplayOrder())
                .build();
    }

    public ProductDto.Image toProductDtoImage(ProductCreateRequest.ProductImageDto request) {
        return ProductDto.Image.builder()
                .url(request.getUrl())
                .altText(request.getAltText())
                .isPrimary(request.getIsPrimary())
                .displayOrder(request.getDisplayOrder())
                .build();
    }

    public ProductDto.ListRequest toProductDtoListRequest(ProductListRequest request) {
        return ProductDto.ListRequest.builder()
                .status(request.getStatus())
                .minPrice(request.getMinPrice())
                .maxPrice(request.getMaxPrice())
                .category(request.getCategory())
                .seller(request.getSeller())
                .brand(request.getBrand())
                .inStock(request.getInStock())
                .tag(request.getTag())
                .search(request.getSearch())
                .createdFrom(request.getCreatedFrom())
                .createdTo(request.getCreatedTo())
                .pagination(toPaginationInfo(request))
                .build();
    }

    public PaginationDto.PaginationRequest toPaginationInfo(ProductListRequest request) {
        return PaginationDto.PaginationRequest.builder()
                .page(request.getPage())
                .size(request.getPage())
                .sort(request.getSort())
                .build();
    }
}

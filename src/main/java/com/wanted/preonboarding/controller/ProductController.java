package com.wanted.preonboarding.controller;

import com.wanted.preonboarding.controller.dto.request.ProductImageRequest;
import com.wanted.preonboarding.controller.dto.request.ProductOptionRequest;
import com.wanted.preonboarding.controller.dto.request.ProductUpdateRequest;
import com.wanted.preonboarding.controller.dto.response.ApiResponse;
import com.wanted.preonboarding.controller.dto.request.ProductCreateRequest;
import com.wanted.preonboarding.controller.dto.request.ProductListRequest;
import com.wanted.preonboarding.controller.dto.response.ProductListResponse;
import com.wanted.preonboarding.controller.mapper.ProductControllerMapper;
import com.wanted.preonboarding.service.ProductService;
import com.wanted.preonboarding.service.product.ProductDto;
import com.wanted.preonboarding.service.product.command.ProductCommand;
import com.wanted.preonboarding.service.product.command.ProductCommandHandler;
import com.wanted.preonboarding.service.product.query.ProductQuery;
import com.wanted.preonboarding.service.product.query.ProductQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductCommandHandler productCommandHandler;
    private final ProductQueryHandler productQueryHandler;
    private final ProductControllerMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto.Product>> addProduct(@RequestBody ProductCreateRequest request) {
        log.info("request: {}", request);
        ProductCommand.CreateProduct command = mapper.toProductDtoCreateRequest(request);
        ProductDto.Product createdProduct = productCommandHandler.createProduct(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdProduct, "상품이 성공적으로 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(@ParameterObject ProductListRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                productQueryHandler.getProducts(mapper.toProductDtoListRequest(request)),
                "상품 목록을 정상적으로 조회했습니다."
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto.Product>> getProduct(@PathVariable Long id) {
        ProductQuery.GetProduct query = ProductQuery.GetProduct.builder()
                .productId(id)
                .build();
        ProductDto.Product foundProduct = productQueryHandler.getProduct(query);
        return ResponseEntity.ok(ApiResponse.success(foundProduct, "상품 상세 정보를 성공적으로 조회했습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto.Product>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request
    ) {
        ProductCommand.UpdateProduct command = mapper.toServiceUpdateDto(request);
        ProductDto.Product updatedProduct = productCommandHandler.updateProduct(command);
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "상품이 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        ProductCommand.DeleteProduct command = ProductCommand.DeleteProduct.builder()
                .productId(id)
                .build();
        productCommandHandler.deleteProduct(command);
        return ResponseEntity.ok(ApiResponse.success(null, "상품이 성공적으로 삭제되었습니다."));
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<ApiResponse<ProductDto.Option>> addProductOption(
            @PathVariable Long id,
            @RequestParam Long optionGroupId,
            @RequestBody ProductOptionRequest request
    ) {
        ProductCommand.AddProductOption command = ProductCommand.AddProductOption.builder()
                .productId(id)
                .option(mapper.toProductDtoOptionWithOptionGroupId(optionGroupId, request))
                .build();
        ProductDto.Option createdOption = productCommandHandler.addProductOption(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdOption, "상품 옵션이 성공적으로 추가되었습니다."));
    }

    @PutMapping("/{id}/options/{optionId}")
    public ResponseEntity<ApiResponse<ProductDto.Option>> updateProductOption(
            @PathVariable Long id,
            @PathVariable Long optionId,
            @RequestBody ProductOptionRequest request
    ) {
        ProductCommand.UpdateProductOption command = ProductCommand.UpdateProductOption.builder()
                .productId(id)
                .option(mapper.toProductDtoOptionWithOptionId(optionId, request))
                .build();
        ProductDto.Option updatedOption = productCommandHandler.updateProductOption(command);
        return ResponseEntity.ok(ApiResponse.success(updatedOption, "상품 옵션이 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/{id}/options/{optionId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductOption(
            @PathVariable Long id,
            @PathVariable Long optionId
    ) {
        ProductCommand.DeleteProductOption command = ProductCommand.DeleteProductOption.builder()
                .productId(id)
                .optionId(optionId)
                .build();
        productCommandHandler.deleteProductOption(command);
        return ResponseEntity.ok(ApiResponse.success(null, "상품 옵션이 성공적으로 삭제되었습니다."));
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ApiResponse<ProductDto.Image>> addProductImage(
            @PathVariable Long id,
            @RequestBody ProductImageRequest request
    ) {
        ProductCommand.AddProductImage command = ProductCommand.AddProductImage.builder()
                .productId(id)
                .image(mapper.toProductDtoImage(request))
                .build();
        ProductDto.Image createdImage = productCommandHandler.addProductImage(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdImage, "상품 이미지가 성공적으로 추가되었습니다."));
    }
}

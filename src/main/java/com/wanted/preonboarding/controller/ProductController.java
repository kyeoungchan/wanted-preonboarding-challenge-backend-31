package com.wanted.preonboarding.controller;

import com.wanted.preonboarding.controller.dto.ApiResponse;
import com.wanted.preonboarding.controller.dto.request.ProductCreateRequest;
import com.wanted.preonboarding.controller.mapper.ControllerProductMapper;
import com.wanted.preonboarding.service.ProductService;
import com.wanted.preonboarding.service.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ControllerProductMapper controllerProductMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto.Product>> addProduct(@RequestBody ProductCreateRequest request) {

        ProductDto.CreateRequest createRequest = controllerProductMapper.toProductDtoCreateRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(, "상품이 성공적으로 등록되었습니다."));
    }
}

package com.wanted.preonboarding.service.product.query;

import com.wanted.preonboarding.controller.dto.response.ProductListResponse;
import com.wanted.preonboarding.service.product.ProductDto;

public interface ProductQueryHandler {
    // 상품 조회
    ProductDto.Product getProduct(ProductQuery.GetProduct query);

    ProductListResponse getProducts(ProductQuery.ListProducts query);
}

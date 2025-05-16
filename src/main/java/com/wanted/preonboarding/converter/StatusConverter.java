package com.wanted.preonboarding.converter;

import com.wanted.preonboarding.constant.ProductStatus;
import jakarta.persistence.AttributeConverter;

public class StatusConverter implements AttributeConverter<ProductStatus, String> {
    @Override
    public String convertToDatabaseColumn(ProductStatus productStatus) {
        if (productStatus == null) {
            return null;
        }
        return productStatus.name();
    }

    @Override
    public ProductStatus convertToEntityAttribute(String s) {
        return ProductStatus.getInstance(s);
    }
}

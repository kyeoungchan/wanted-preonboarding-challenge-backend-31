package com.wanted.preonboarding.repository.projection;

import java.math.BigDecimal;

public interface ProductOptionProjection {
    Long getId();
    String getName();
    BigDecimal getAdditionalPrice();
    String getSku();
    Integer getStock();
    Integer getDisplayOrder();
    Long getOptionGroupId();
}

package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.entity.Product;
import java.util.List;

public interface ProductCustomRepository {
    List<Product> findProductsByName(String name);
}

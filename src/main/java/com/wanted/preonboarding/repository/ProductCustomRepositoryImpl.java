package com.wanted.preonboarding.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.preonboarding.entity.Product;
import com.wanted.preonboarding.entity.QProduct;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findProductsByName(String name) {
        QProduct product = QProduct.product;
        return queryFactory.selectFrom(product)
                .where(product.name.containsIgnoreCase(name))
                .fetch();
    }
}

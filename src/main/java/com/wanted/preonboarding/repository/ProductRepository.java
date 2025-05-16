package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository, JpaSpecificationExecutor<Product> {
    // 카테고리 ID로 상품 조회
    Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);

    // 여러 카테고리 ID로 상품 조회(하위 카테고리 포함)
    Page<Product> findByCategoriesIdIn(List<Long> categoriesId, Pageable pageable);
}
package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.constant.ProductStatus;
import com.wanted.preonboarding.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository, JpaSpecificationExecutor<Product> {
    // 카테고리 ID로 상품 조회
    Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);

    // 여러 카테고리 ID로 상품 조회(하위 카테고리 포함)
    Page<Product> findByCategoriesIdIn(List<Long> categoriesId, Pageable pageable);

    // 상태별 상품 조회 (생성일 기준 정렬)
    List<Product> findTop5ByStatusOrderByCreatedAtDesc(ProductStatus status);

    // 인기 상품 조회(리뷰 평점 기준)
    @Query(value = "select p.* from products p " +
            "join reviews r on p.id = r.product_id " +
            "where p.status = 'ACTIVE' " +
            "group by p.id " +
            "order by avg(r.rating) desc, count(r.id) desc " +
            "limit 5",
            nativeQuery = true)
    List<Product> findTop5PopularProducts();

    // 카테고리 별 상품 수 카운트
    @Query("select c.id, count(p) " +
            "from Product p join p.categories c " +
            "where p.status = 'ACTIVE' " +
            "group by c.id")
    List<Object[]> countProductsByCategories();
}
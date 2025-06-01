package com.wanted.preonboarding.service.repository;

import com.wanted.preonboarding.service.entity.ProductOption;
import com.wanted.preonboarding.service.repository.projection.ProductOptionProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    @Query("SELECT o.id as id, o.name as name, o.additionalPrice as additionalPrice, " +
            "o.sku as sku, o.stock as stock, o.displayOrder as displayOrder, og.id as optionGroupId " +
            "FROM ProductOption o " +
            "JOIN o.optionGroup og " +
            "WHERE og.id = :optionGroupId")
    List<ProductOptionProjection> findOptionsByOptionGroupId(@Param("optionGroupId") Long optionGroupId);

    @Query("SELECT  o.id as id, o.name as name, o.additionalPrice as additionalPrice, " +
            "o.sku as sku, o.stock as stock, o.displayOrder as displayOrder, og.id as optionGroupId " +
            "FROM ProductOption o " +
            "JOIN o.optionGroup og " +
            "JOIN og.product p " +
            "WHERE p.id = :productId")
    List<ProductOptionProjection> findOptionsByProductId(@Param("productId") Long productId);

    @Query("SELECT o.optionGroup.product.id FROM ProductOption o WHERE o.id = :optionId")
    Long findProductIdByOptionId(@Param("optionId") Long optionId);
}

package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 레벨별 카테고리 조회
    List<Category> findByLevel(int level);
}

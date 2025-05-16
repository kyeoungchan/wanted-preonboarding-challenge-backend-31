package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

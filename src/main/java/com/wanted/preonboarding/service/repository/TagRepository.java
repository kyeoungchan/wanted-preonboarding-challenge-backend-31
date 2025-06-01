package com.wanted.preonboarding.service.repository;

import com.wanted.preonboarding.service.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

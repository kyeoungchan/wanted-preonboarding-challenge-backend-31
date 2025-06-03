package com.wanted.preonboarding.service.query.repository;

import com.wanted.preonboarding.service.query.entity.BrandDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandDocumentRepository extends MongoRepository<BrandDocument, Long> {
}

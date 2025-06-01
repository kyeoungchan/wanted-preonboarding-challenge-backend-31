package com.wanted.preonboarding.service.query.repository;

import com.wanted.preonboarding.service.query.entity.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDocumentRepository extends MongoRepository<ProductDocument, Long> {
}

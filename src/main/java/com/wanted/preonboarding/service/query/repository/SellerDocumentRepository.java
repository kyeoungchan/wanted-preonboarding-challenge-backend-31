package com.wanted.preonboarding.service.query.repository;

import com.wanted.preonboarding.service.query.entity.SellerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerDocumentRepository extends MongoRepository<SellerDocument, Long> {
}

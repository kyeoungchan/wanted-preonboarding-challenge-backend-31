package com.wanted.preonboarding.service.query.repository;

import com.wanted.preonboarding.service.query.entity.TagDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagDocumentRepository extends MongoRepository<TagDocument, Long> {
}

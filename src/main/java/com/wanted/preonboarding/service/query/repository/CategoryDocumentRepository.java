package com.wanted.preonboarding.service.query.repository;

import com.wanted.preonboarding.service.query.entity.CategoryDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CategoryDocumentRepository extends MongoRepository<CategoryDocument, String> {

    @Query("{'level':  ?0}")
    List<CategoryDocument> findByLevel(String level);

    @Query("{'parent.id':  ?0}")
    List<CategoryDocument> findByParentId(String parentId);
}

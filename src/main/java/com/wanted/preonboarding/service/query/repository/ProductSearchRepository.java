package com.wanted.preonboarding.service.query.repository;

import com.wanted.preonboarding.service.query.entity.ProductSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearchDocument, Long> {
}

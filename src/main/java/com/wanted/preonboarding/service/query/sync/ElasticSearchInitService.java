package com.wanted.preonboarding.service.query.sync;

import com.wanted.preonboarding.service.query.entity.ProductSearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;

/**
 * Elasticsearch 인덱스를 초기화하는 서비스
 * 애플리케이션 시작 시 인덱스가 없으면 생성하고, 설정을 적용함
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchInitService {
    private final ElasticsearchOperations elasticsearchOperations;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndices() {
        try {
            log.info("Initializing ElasticSearch Indices!!!");

            // 상품 인덱스 설정
            IndexOperations indexOperations = elasticsearchOperations.indexOps(ProductSearchDocument.class);

            // 인덱스가 존재하지 않으면 생성
            if (!indexOperations.exists()) {
                log.info("Creating products index with Nori analyzer");
                indexOperations.create();
                indexOperations.putMapping(indexOperations.createMapping());
            }
            log.info("ElasticSearch Indices initialized successfully");
        } catch (Exception e) {
            log.error("ElasticSearch Indices initialization failed", e);
        }
    }
}

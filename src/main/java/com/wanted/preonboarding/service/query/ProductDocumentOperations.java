package com.wanted.preonboarding.service.query;

import com.wanted.preonboarding.service.dto.MainPageDto;
import com.wanted.preonboarding.service.query.entity.CategoryDocument;
import com.wanted.preonboarding.service.query.entity.ProductDocument;
import com.wanted.preonboarding.service.query.repository.CategoryDocumentRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductDocumentOperations {

    private final MongoTemplate mongoTemplate;
    private final CategoryDocumentRepository productDocumentRepository;
    private final CategoryDocumentRepository categoryDocumentRepository;

    public ProductDocument findProductDocumentWithReferences(Long productId) {
        try {
            // 직접 Aggregation 파이프라인을 구성 - MongoDB의 기본 Document 사용
            List<Document> pipeline = buildAggregatePipeline(productId);

            // MongoDB에서 결과 조회
            Document result = mongoTemplate.getCollection("products")
                    .aggregate(pipeline)
                    .first();

            if (result == null) {
                return null;
            }

            // Document에서 ProductDocument로 변환
            ProductDocument productDocument = mongoTemplate.getConverter()
                    .read(ProductDocument.class, result);

            return productDocument;
        } catch (Exception e) {
            log.error("Error finding product document: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<ProductDocument> findProductDocumentsWithReferences(List<Long> productIds) {
        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // 직접 Aggregation 파이프라인을 구성
            List<Document> pipeline = buildAggregatePipeLineForMultiple(productIds);

            // MongoDB에서 결과 조회
            List<Document> results = new ArrayList<>();
            mongoTemplate.getCollection("products")
                    .aggregate(pipeline)
                    .into(results);

            if (results.isEmpty()) {
                return Collections.emptyList();
            }

            // Document 목록에서 ProductDocument 목록으로 변환
            List<ProductDocument> productDocuments = new ArrayList<>();
            for (Document doc : results) {
                ProductDocument productDocument = mongoTemplate.getConverter().read(ProductDocument.class, doc);
                productDocuments.add(productDocument);
            }

            // ID 순서에 맞게 정렬
            Map<Long, ProductDocument> productMap = new HashMap<>(productDocuments.size());
            for (ProductDocument doc : productDocuments) {
                productMap.put(doc.getId(), doc);
            }

            List<ProductDocument> orderedResults = new ArrayList<>(productIds.size());
            for (Long id : productIds) {
                ProductDocument product = productMap.get(id);
                if (product != null) {
                    orderedResults.add(product);
                }
            }

            return orderedResults;
        } catch (Exception e) {
            log.error("Error finding product documents: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 단일 상품을 위한 MongoDB Aggregation 파이프라인 구성
     */
    private List<Document> buildAggregatePipeline(Long productId) {
        List<Document> pipeline = new ArrayList<>();

        // 1. 상품 ID로 필터링
        pipeline.add(new Document("$match", new Document("_id", productId)));

        // 공통 Aggregation 추가
        pipeline.addAll(commonAggregatePipeline());

        return pipeline;
    }

    /**
     * 여러 상품을 위한 MongoDB Aggregation 파이프라인 구성
     */
    private List<Document> buildAggregatePipeLineForMultiple(List<Long> productIds) {
        List<Document> pipeline = new ArrayList<>();

        // 1. 상품 ID 목록으로 필터링
        pipeline.add(new Document("$match", new Document("_id", new Document("$in", productIds))));

        // 공통 Aggregation 추가
        pipeline.addAll(commonAggregatePipeline());

        return pipeline;
    }

    /**
     * 공통 MongoDB Aggregation 파이프라인 단계
     */
    private List<Document> commonAggregatePipeline() {
        List<Document> pipeline = new ArrayList<>();

        // 1. 브랜드 정보 확인
        pipeline.add(new Document("$lookup",
                new Document("from", "brands")
                        .append("localField", "brand._id")
                        .append("foreignField", "_id")
                        .append("as", "brandDetails")));

        // 브랜드 정보 매핑
        pipeline.add(new Document("$addFields",
                new Document("brand",
                        new Document("$cond", Arrays.asList(
                                new Document("$gt", Arrays.asList(
                                        new Document("$size", "$brandDetails"), 0)),
                                new Document("$mergeObjects", Arrays.asList(
                                        "$brand",
                                        new Document("name", new Document("$arrayElemAt", Arrays.asList("$brandDetails.name", 0))),
                                        new Document("slug", new Document("$arrayElemAt", Arrays.asList("$brandDetails.slug", 0)))
                                )),
                                "$brand"
                        ))
                )));

        // 2. 판매자 정보 조인Add commentMore actions
        pipeline.add(new Document("$lookup",
                new Document("from", "sellers")
                        .append("localField", "seller._id")
                        .append("foreignField", "_id")
                        .append("as", "sellerDetails")));

        // 판매자 정보 매핑
        pipeline.add(new Document("$addFields",
                new Document("seller",
                        new Document("$cond", Arrays.asList(
                                new Document("$gt", Arrays.asList(
                                        new Document("$size", "$sellerDetails"), 0)),
                                new Document("$mergeObjects", Arrays.asList(
                                        "$seller",
                                        new Document("name", new Document("$arrayElemAt", Arrays.asList("$sellerDetails.name", 0)))
                                )),
                                "$seller"
                        ))
                )));

        // 3. 태그 정보 조인
        pipeline.add(new Document("$lookup",
                new Document("from", "tags")
                        .append("localField", "tags._id")
                        .append("foreignField", "_id")
                        .append("as", "tagDetails")));

        // 태그 정보 매핑
        pipeline.add(new Document("$addFields",
                new Document("tags",
                        new Document("$cond", Arrays.asList(
                                new Document("$isArray", "$tags"),
                                new Document("$map",
                                        new Document("input", "$tags")
                                                .append("as", "tag")
                                                .append("in",
                                                        new Document("$mergeObjects", Arrays.asList(
                                                                "$$tag",
                                                                new Document("$let",
                                                                        new Document("vars",
                                                                                new Document("matchedTag",
                                                                                        new Document("$arrayElemAt", Arrays.asList(
                                                                                                new Document("$filter",
                                                                                                        new Document("input", "$tagDetails")
                                                                                                                .append("as", "t")
                                                                                                                .append("cond",
                                                                                                                        new Document("$eq", Arrays.asList("$$t._id", "$$tag._id"))
                                                                                                                )
                                                                                                ),
                                                                                                0
                                                                                        ))
                                                                                )
                                                                        )
                                                                                .append("in",
                                                                                        new Document("$cond", Arrays.asList(
                                                                                                "$$matchedTag",
                                                                                                new Document()
                                                                                                        .append("name", "$$matchedTag.name")
                                                                                                        .append("slug", "$$matchedTag.slug"),
                                                                                                new Document()
                                                                                        ))
                                                                                )
                                                                )
                                                        ))
                                                )
                                ),
                                "$tags"
                        ))
                )));

        // 4. 카테고리 정보 조인
        pipeline.add(new Document("$lookup",
                new Document("from", "categories")
                        .append("localField", "categories._id")
                        .append("foreignField", "_id")
                        .append("as", "categoryDetails")));

        // 카테고리 정보 매핑
        pipeline.add(new Document("$addFields",
                new Document("categories",
                        new Document("$cond", Arrays.asList(
                                new Document("$isArray", "$categories"),
                                new Document("$map",
                                        new Document("input", "$categories")
                                                .append("as", "category")
                                                .append("in",
                                                        new Document("$mergeObjects", Arrays.asList(
                                                                "$$category",
                                                                new Document("$let",
                                                                        new Document("vars",
                                                                                new Document("matchedCategory",
                                                                                        new Document("$arrayElemAt", Arrays.asList(
                                                                                                new Document("$filter",
                                                                                                        new Document("input", "$categoryDetails")
                                                                                                                .append("as", "c")
                                                                                                                .append("cond",
                                                                                                                        new Document("$eq", Arrays.asList("$$c._id", "$$category._id"))
                                                                                                                )
                                                                                                ),
                                                                                                0
                                                                                        ))
                                                                                )
                                                                        )
                                                                                .append("in",
                                                                                        new Document("$cond", Arrays.asList(
                                                                                                "$$matchedCategory",
                                                                                                new Document()
                                                                                                        .append("name", "$$matchedCategory.name")
                                                                                                        .append("slug", "$$matchedCategory.slug")
                                                                                                        .append("parent", "$$matchedCategory.parent"),
                                                                                                new Document()
                                                                                        ))
                                                                                )
                                                                )
                                                        ))
                                                )
                                ),
                                "$categories"
                        ))
                )));

        // 5. 임시 조인 컬렉션 제거
        pipeline.add(new Document("$project",
                new Document("brandDetails", 0)
                        .append("sellerDetails", 0)
                        .append("categoryDetails", 0)
                        .append("tagDetails", 0)));
        return pipeline;
    }

    public List<ProductDocument> findNewProducts(int limit) {
        Query query = new Query()
                .addCriteria(Criteria.where("status").is("ACTIVE"))
                .with(Sort.by(Sort.Direction.DESC, "createdAt"))
                .limit(limit);

        query.fields().include("_id");

        List<Long> foundIds = mongoTemplate.find(query, ProductDocument.class).stream()
                .map(ProductDocument::getId)
                .toList();

        return findProductDocumentsWithReferences(foundIds);
    }

    public List<ProductDocument> findPopularProducts(int limit) {
        Query query = new Query()
                .addCriteria(Criteria.where("status").is("ACTIVE"))
                .addCriteria(Criteria.where("rating.average").exists(true))
                .with(Sort.by(Sort.Direction.DESC, "rating.average", "rating.count"))
                .limit(limit);

        query.fields().include("_id");

        List<Long> foundIds = mongoTemplate.find(query, ProductDocument.class).stream()
                .map(ProductDocument::getId)
                .toList();

        return findProductDocumentsWithReferences(foundIds);
    }

    public List<MainPageDto.FeaturedCategory> findFeaturedCategories(int limit) {
        // 1단계 카테고리 조회
        List<CategoryDocument> categories = categoryDocumentRepository.findByLevel(1);

        // 카테고리별 상품 수 계산을 위한 집계 쿼리
        List<MainPageDto.FeaturedCategory> featuredCategories = categories.stream()
                .map(category -> {
                    // 이 카테고리에 속한 상품 수 집계
                    Query query = new Query();
                    query.addCriteria(Criteria.where("categories.id").is(category.getId()));
                    query.addCriteria(Criteria.where("status").is("ACTIVE"));
                    long productCount = mongoTemplate.count(query, ProductDocument.class);

                    return MainPageDto.FeaturedCategory.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .slug(category.getSlug())
                            .imageUrl(category.getImageUrl())
                            .productCount((int) productCount)
                            .build();
                })
                .filter(c -> c.getProductCount() > 0) // 상품이 있는 카테고리만 필터링
                .sorted(Comparator.comparing(MainPageDto.FeaturedCategory::getProductCount).reversed())
                .limit(limit)
                .toList();

        return featuredCategories;
    }
}

-- ProductRepository.findByCategoriesId, findByCategoriesIdIn에서 사용
CREATE INDEX idx_product_categories_category_id ON product_categories(category_id);
CREATE INDEX idx_product_categories_product_id ON product_categories(product_id);

-- CategoryRepository.findByLevel에서 사용
CREATE INDEX idx_categories_level ON categories(level);

-- ProductSpecification.withStatus와 findTop5ByStatusOrderByCreatedAtDesc에서 사용
CREATE INDEX idx_products_status_created_at ON products(status, created_at DESC);

-- ProductSpecification.withMinPrice, withMaxPrice에서 사용
CREATE INDEX idx_product_prices_base_price ON product_prices(base_price);

-- ProductSpecification.withBrandId에서 사용
CREATE INDEX idx_products_brand_id ON products(brand_id);

-- ProductSpecification.withSellerId에서 사용
CREATE INDEX idx_products_seller_id ON products(seller_id);

-- ProductSpecification.withTagIds에서 사용
CREATE INDEX idx_product_tags_tag_id ON product_tags(tag_id);
CREATE INDEX idx_product_tags_product_id ON product_tags(product_id);

-- ProductSpecification.inStock에서 사용
CREATE INDEX idx_product_options_stock ON product_options(stock);

-- ReviewRepository.findByProductId, findByProductIdAndRating에서 사용
-- MainServiceImpl.getMainPageContents에서 findTop5PopularProducts() 최적화
CREATE INDEX idx_reviews_product_id ON reviews(product_id);
CREATE INDEX idx_reviews_product_id_rating ON reviews(product_id, rating);
package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.constant.ProductStatus;
import com.wanted.preonboarding.entity.Brand;
import com.wanted.preonboarding.entity.Product;
import com.wanted.preonboarding.entity.Seller;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void init() {
        assertNotNull(productRepository);
    }

    @Test
    @DisplayName("상품 추가가 잘 되는가?")
    void saveTest() {
        Seller seller = Seller.builder()
                .name("Tester")
                .description("test")
                .logoUrl("testLogoUrl")
                .build();

        Brand brand = Brand.builder()
                .name("TestBrand")
                .description("test")
                .logoUrl("testLogoUrl")
                .build();

        Product product = Product.builder()
                .seller(seller)
                .brand(brand)
                .slug("slug!!!")
                .status(ProductStatus.ACTIVE)
                .name("productName")
                .build();
        log.info("product: {}", product);

        productRepository.save(product);
    }

    @Test
    @DisplayName("등록한 상품이 조회도 잘 되는가?")
    void inquiryProductTest() {
        Seller seller = Seller.builder()
                .name("Tester")
                .description("test")
                .logoUrl("testLogoUrl")
                .build();

        Brand brand = Brand.builder()
                .name("TestBrand")
                .description("test")
                .logoUrl("testLogoUrl")
                .build();

        Product product = Product.builder()
                .seller(seller)
                .brand(brand)
                .status(ProductStatus.ACTIVE)
                .name("productName")
                .slug("slug!!!")
                .build();
         Long id = productRepository.save(product).getId();

        // then
        Optional<Product> foundProduct = productRepository.findById(id);
        assertNotNull(foundProduct);
        assertTrue(foundProduct.isPresent());
        log.info("product: {}", foundProduct.get());
    }

    @Test
    @DisplayName("상품명으로 검색 querydsl 기능이 잘 작동되는가?")
    void querydslProductTest() {
        Product productA = Product.builder()
                .name("AAAAAAAA")
                .slug("slugA")
                .status(ProductStatus.ACTIVE)
                .build();
        Product productB = Product.builder()
                .name("BBBBBBBB")
                .slug("slugB")
                .status(ProductStatus.ACTIVE)
                .build();
        Product productAB = Product.builder()
                .name("AAABBBBB")
                .slug("slugC")
                .status(ProductStatus.ACTIVE)
                .build();
        productRepository.save(productA);
        productRepository.save(productB);
        productRepository.save(productAB);

        //then
        List<Product> aaa = productRepository.findProductsByName("AAA");
        assertNotNull(aaa);
        assertFalse(aaa.isEmpty());
        for (Product product : aaa) {
            assertTrue(product.getName().contains("AAA"));
            log.info("product: {}", product);
        }
    }
}
package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.constant.Status;
import com.wanted.preonboarding.entity.Brand;
import com.wanted.preonboarding.entity.Product;
import com.wanted.preonboarding.entity.Seller;
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
                .status(Status.ON_SALE)
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
                .status(Status.ON_SALE)
                .name("productName")
                .build();
         Long id = productRepository.save(product).getId();

        // then
        Optional<Product> foundProduct = productRepository.findById(id);
        assertNotNull(foundProduct);
        assertTrue(foundProduct.isPresent());
        log.info("product: {}", foundProduct.get());
    }
}
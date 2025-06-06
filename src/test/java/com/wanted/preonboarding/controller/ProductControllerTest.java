package com.wanted.preonboarding.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.controller.dto.request.ProductCreateRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록이 잘 되는가?")
    void addProduct() throws Exception {
        Map<String, Object> dimensions = new HashMap<>();
        dimensions.put("width", 200);
        dimensions.put("height", 85);
        dimensions.put("depth", 90);

        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("assembly_required", true);
        additionalInfo.put("assembly_time", "30분");

        ProductCreateRequest.ProductDetailDto detailDto = ProductCreateRequest.ProductDetailDto.builder()
                .weight(25.5)
                .dimensions(dimensions)
                .materials("가죽, 목재, 폼")
                .countryOfOrigin("대한민국")
                .warrantyInfo("2년 품질 보증")
                .careInstructions("마른 천으로 표면을 닦아주세요")
                .additionalInfo(additionalInfo)
                .build();

        ProductCreateRequest.ProductPriceDto priceDto = ProductCreateRequest.ProductPriceDto.builder()
                .basePrice(BigDecimal.valueOf(599000))
                .salePrice(BigDecimal.valueOf(499000))
                .costPrice(BigDecimal.valueOf(350000))
                .currency("KRW")
                .taxRate(BigDecimal.valueOf(10))
                .build();

        ProductCreateRequest.ProductCategoryDto categoryDto1 = ProductCreateRequest.ProductCategoryDto.builder()
                .categoryId(5L)
                .isPrimary(true)
                .build();
        ProductCreateRequest.ProductCategoryDto categoryDto2 = ProductCreateRequest.ProductCategoryDto.builder()
                .categoryId(8L)
                .isPrimary(false)
                .build();

        ProductCreateRequest.ProductOptionDto option11 = ProductCreateRequest.ProductOptionDto.builder()
                .name("브라운")
                .addtionalPrice(BigDecimal.valueOf(0))
                .sku("SOFA-BRN")
                .stock(10)
                .displayOrder(1)
                .build();

        ProductCreateRequest.ProductOptionDto option12 = ProductCreateRequest.ProductOptionDto.builder()
                .name("블랙")
                .addtionalPrice(BigDecimal.valueOf(0))
                .sku("SOFA-BLK")
                .stock(15)
                .displayOrder(2)
                .build();

        ProductCreateRequest.ProductOptionGroupDto optionGroupDto1 = ProductCreateRequest.ProductOptionGroupDto.builder()
                .name("색상")
                .displayOrder(1)
                .options(List.of(option11, option12))
                .build();

        ProductCreateRequest.ProductOptionDto option21 = ProductCreateRequest.ProductOptionDto.builder()
                .name("천연 가죽")
                .addtionalPrice(BigDecimal.valueOf(100000))
                .sku("SOFA-LTHR")
                .stock(5)
                .displayOrder(1).build();

        ProductCreateRequest.ProductOptionDto option22 = ProductCreateRequest.ProductOptionDto.builder()
                .name("인조 가죽")
                .addtionalPrice(BigDecimal.valueOf(0))
                .sku("SOFA-FAKE")
                .stock(20)
                .displayOrder(2)
                .build();

        ProductCreateRequest.ProductOptionGroupDto optionGroupDto2 = ProductCreateRequest.ProductOptionGroupDto.builder()
                .name("소재")
                .displayOrder(2)
                .options(List.of(option21, option22))
                .build();

        ProductCreateRequest.ProductImageDto imageDto1 = ProductCreateRequest.ProductImageDto.builder()
                .url("https://example.com/images/sofa1.jpg")
                .altText("브라운 소파 정면")
                .isPrimary(true)
                .displayOrder(1)
                .optionId(null)
                .build();

        ProductCreateRequest.ProductImageDto imageDto2 = ProductCreateRequest.ProductImageDto.builder()
                .url("https://example.com/images/sofa2.jpg")
                .altText("브라운 소파 측면")
                .isPrimary(false)
                .displayOrder(2)
                .optionId(null)
                .build();


        ProductCreateRequest createRequest = ProductCreateRequest.builder()
                .name("슈퍼 편안한 소파")
                .slug("super-comfortable-sofa2")
                .shortDescription("최고급 소재로 만든 편안한 소파")
                .fullDescription("<p>이 소파는 최고급 소재로 제작되었으며...</p>")
                .sellerId(1L)
                .brandId(2L)
                .status("ACTIVE")
                .detail(detailDto)
                .price(priceDto)
                .categories(List.of(categoryDto1, categoryDto2))
                .optionGroups(List.of(optionGroupDto1, optionGroupDto2))
                .images(List.of(imageDto1, imageDto2))
                .tags(List.of(1L, 4L, 7L))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(createRequest);
        String requestJson2 = "{" +
                " \"name\": \"슈퍼 편안한 소파\",\n" +
                " \"slug\": \"super-comfortable-sofa2\",\n" +
                " \"short_description\": \"최고급 소재로 만든 편안한 소파\",\n" +
                " \"full_description\": \"<p>이 소파는 최고급 소재로 제작되었으며...</p>\",\n" +
                " \"seller_id\": 1,\n" +
                " \"brand_id\": 2,\n" +
                " \"status\": \"ACTIVE\",\n" +
                " \"detail\": {\n" +
                "   \"weight\": 25.5,\n" +
                "   \"dimensions\": {\n" +
                "     \"width\": 200,\n" +
                "     \"height\": 85,\n" +
                "     \"depth\": 90\n" +
                "   },\n" +
                "   \"materials\": \"가죽, 목재, 폼\",\n" +
                "   \"country_of_origin\": \"대한민국\",\n" +
                "   \"warranty_info\": \"2년 품질 보증\",\n" +
                "   \"care_instructions\": \"마른 천으로 표면을 닦아주세요\",\n" +
                "   \"additional_info\": {\n" +
                "     \"assembly_required\": true,\n" +
                "     \"assembly_time\": \"30분\"\n" +
                "   }\n" +
                " },\n" +
                " \"price\": {\n" +
                "   \"base_price\": 599000,\n" +
                "   \"sale_price\": 499000,\n" +
                "   \"cost_price\": 350000,\n" +
                "   \"currency\": \"KRW\",\n" +
                "   \"tax_rate\": 10\n" +
                " },\n" +
                " \"categories\": [\n" +
                "   {\n" +
                "     \"category_id\": 5,\n" +
                "     \"is_primary\": true\n" +
                "   },\n" +
                "   {\n" +
                "     \"category_id\": 8,\n" +
                "     \"is_primary\": false\n" +
                "   }\n" +
                " ],\n" +
                " \"option_groups\": [\n" +
                "   {\n" +
                "     \"name\": \"색상\",\n" +
                "     \"display_order\": 1,\n" +
                "     \"options\": [\n" +
                "       {\n" +
                "         \"name\": \"브라운\",\n" +
                "         \"additional_price\": 0,\n" +
                "         \"sku\": \"SOFA-BRN\",\n" +
                "         \"stock\": 10,\n" +
                "         \"display_order\": 1\n" +
                "       },\n" +
                "       {\n" +
                "         \"name\": \"블랙\",\n" +
                "         \"additional_price\": 0,\n" +
                "         \"sku\": \"SOFA-BLK\",\n" +
                "         \"stock\": 15,\n" +
                "         \"display_order\": 2\n" +
                "       }\n" +
                "     ]\n" +
                "   },\n" +
                "   {\n" +
                "     \"name\": \"소재\",\n" +
                "     \"display_order\": 2,\n" +
                "     \"options\": [\n" +
                "       {\n" +
                "         \"name\": \"천연 가죽\",\n" +
                "         \"additional_price\": 100000,\n" +
                "         \"sku\": \"SOFA-LTHR\",\n" +
                "         \"stock\": 5,\n" +
                "         \"display_order\": 1\n" +
                "       },\n" +
                "       {\n" +
                "         \"name\": \"인조 가죽\",\n" +
                "         \"additional_price\": 0,\n" +
                "         \"sku\": \"SOFA-FAKE\",\n" +
                "         \"stock\": 20,\n" +
                "         \"display_order\": 2\n" +
                "       }\n" +
                "     ]\n" +
                "   }\n" +
                " ],\n" +
                " \"images\": [\n" +
                "   {\n" +
                "     \"url\": \"https://example.com/images/sofa1.jpg\",\n" +
                "     \"alt_text\": \"브라운 소파 정면\",\n" +
                "     \"is_primary\": true,\n" +
                "     \"display_order\": 1,\n" +
                "     \"option_id\": null\n" +
                "   },\n" +
                "   {\n" +
                "     \"url\": \"https://example.com/images/sofa2.jpg\",\n" +
                "     \"alt_text\": \"브라운 소파 측면\",\n" +
                "     \"is_primary\": false,\n" +
                "     \"display_order\": 2,\n" +
                "     \"option_id\": null\n" +
                "   }\n" +
                " ],\n" +
                " \"tags\": [1, 4, 7]\n" +
                "}";

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson2))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("슈퍼 편안한 소파"))
                .andExpect(jsonPath("$.data.slug").value("super-comfortable-sofa2"))
                .andDo(print());


    }

    @Test
    @DisplayName("상품 전체 조회가 잘 되는가?")
    void getProducts() throws Exception {
        mockMvc.perform(get("/api/products?page=1&perPage=10&sort=created_at:desc&status=ACTIVE&minPrice=10000&maxPrice=100000&category=5&seller=1&brand=2&inStock=true&search=소파")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.message").value("상품 목록을 정상적으로 조회했습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 단건 조회가 잘 되는가?")
    void getProduct() throws Exception {
        mockMvc.perform(get("/api/products/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("슈퍼 편안한 소파"))
                .andExpect(jsonPath("$.data.slug").value("super-comfortable-sofa"))
                .andExpect(jsonPath("$.data.seller.id").value(1))
                .andExpect(jsonPath("$.data.brand.id").value(1))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.message").value("상품 상세 정보를 성공적으로 조회했습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정이 잘 되는가?")
    void updateProduct() throws Exception {
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        " \"name\": \"업데이트된 슈퍼 편안한 소파\",\n" +
                        " \"slug\": \"updated-super-comfortable-sofa\",\n" +
                        " \"short_description\": \"업데이트된 최고급 소재로 만든 편안한 소파\",\n" +
                        " \"full_description\": \"<p>이 소파는 최고급 소재로 제작되었으며...</p>\",\n" +
                        " \"seller_id\": 1,\n" +
                        " \"brand_id\": 2,\n" +
                        " \"status\": \"ACTIVE\",\n" +
                        " \"detail\": {\n" +
                        "   \"weight\": 25.5,\n" +
                        "   \"dimensions\": {\n" +
                        "     \"width\": 200,\n" +
                        "     \"height\": 85,\n" +
                        "     \"depth\": 90\n" +
                        "   },\n" +
                        "   \"materials\": \"고급 가죽, 단단한 목재, 고밀도 폼\",\n" +
                        "   \"country_of_origin\": \"대한민국\",\n" +
                        "   \"warranty_info\": \"3년 품질 보증\",\n" +
                        "   \"care_instructions\": \"마른 천으로 표면을 닦아주세요\",\n" +
                        "   \"additional_info\": {\n" +
                        "     \"assembly_required\": true,\n" +
                        "     \"assembly_time\": \"30분\"\n" +
                        "   }\n" +
                        " },\n" +
                        " \"price\": {\n" +
                        "   \"base_price\": 699000,\n" +
                        "   \"sale_price\": 599000,\n" +
                        "   \"cost_price\": 450000,\n" +
                        "   \"currency\": \"KRW\",\n" +
                        "   \"tax_rate\": 10\n" +
                        " },\n" +
                        " \"categories\": [\n" +
                        "   {\n" +
                        "     \"category_id\": 5,\n" +
                        "     \"is_primary\": true\n" +
                        "   },\n" +
                        "   {\n" +
                        "     \"category_id\": 8,\n" +
                        "     \"is_primary\": false\n" +
                        "   }\n" +
                        " ],\n" +
                        " \"option_groups\": [\n" +
                        "   {\n" +
                        "     \"name\": \"색상\",\n" +
                        "     \"display_order\": 1,\n" +
                        "     \"options\": [\n" +
                        "       {\n" +
                        "         \"name\": \"브라운\",\n" +
                        "         \"additional_price\": 0,\n" +
                        "         \"sku\": \"SOFA-BRN\",\n" +
                        "         \"stock\": 10,\n" +
                        "         \"display_order\": 1\n" +
                        "       },\n" +
                        "       {\n" +
                        "         \"name\": \"블랙\",\n" +
                        "         \"additional_price\": 0,\n" +
                        "         \"sku\": \"SOFA-BLK\",\n" +
                        "         \"stock\": 15,\n" +
                        "         \"display_order\": 2\n" +
                        "       }\n" +
                        "     ]\n" +
                        "   },\n" +
                        "   {\n" +
                        "     \"name\": \"소재\",\n" +
                        "     \"display_order\": 2,\n" +
                        "     \"options\": [\n" +
                        "       {\n" +
                        "         \"name\": \"천연 가죽\",\n" +
                        "         \"additional_price\": 100000,\n" +
                        "         \"sku\": \"SOFA-LTHR\",\n" +
                        "         \"stock\": 5,\n" +
                        "         \"display_order\": 1\n" +
                        "       },\n" +
                        "       {\n" +
                        "         \"name\": \"인조 가죽\",\n" +
                        "         \"additional_price\": 0,\n" +
                        "         \"sku\": \"SOFA-FAKE\",\n" +
                        "         \"stock\": 20,\n" +
                        "         \"display_order\": 2\n" +
                        "       }\n" +
                        "     ]\n" +
                        "   }\n" +
                        " ],\n" +
                        " \"images\": [\n" +
                        "   {\n" +
                        "     \"url\": \"https://example.com/images/sofa1.jpg\",\n" +
                        "     \"alt_text\": \"브라운 소파 정면\",\n" +
                        "     \"is_primary\": true,\n" +
                        "     \"display_order\": 1,\n" +
                        "     \"option_id\": null\n" +
                        "   },\n" +
                        "   {\n" +
                        "     \"url\": \"https://example.com/images/sofa2.jpg\",\n" +
                        "     \"alt_text\": \"브라운 소파 측면\",\n" +
                        "     \"is_primary\": false,\n" +
                        "     \"display_order\": 2,\n" +
                        "     \"option_id\": null\n" +
                        "   }\n" +
                        " ],\n" +
                        " \"tags\": [1, 4, 7]\n" +
                        "}"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("상품 삭제가 잘 되는가?")
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("상품이 성공적으로 삭제되었습니다."))
                .andDo(print());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("DELETED"))
                .andDo(print());
    }

    @Test
    @DisplayName("옵션 추가가 잘 되는가?")
    void addProductOption() throws Exception {
        mockMvc.perform(post("/api/products/1/options?optionGroupId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"option_group_id\": 1,\n" +
                                " \"name\": \"네이비\",\n" +
                                " \"additional_price\": 20000,\n" +
                                " \"sku\": \"SOFA-NVY\",\n" +
                                " \"stock\": 8,\n" +
                                " \"display_order\": 3\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.option_group_id").value(1))
                .andExpect(jsonPath("$.data.name").value("네이비"))
                .andExpect(jsonPath("$.data.additional_price").value(20000))
                .andExpect(jsonPath("$.data.sku").value("SOFA-NVY"))
                .andExpect(jsonPath("$.data.stock").value(8))
                .andExpect(jsonPath("$.data.display_order").value(3))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 추가되었습니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("옵션 수정이 잘 되는가?")
    void updateProductOption() throws Exception {
        mockMvc.perform(put("/api/products/9/options/40")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"option_group_id\": 15,\n" +
                                " \"name\": \"네이비\",\n" +
                                " \"additional_price\": 20000,\n" +
                                " \"sku\": \"SOFA-NVY\",\n" +
                                " \"stock\": 8,\n" +
                                " \"display_order\": 3\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.option_group_id").value(15))
                .andExpect(jsonPath("$.data.name").value("네이비"))
                .andExpect(jsonPath("$.data.additional_price").value(20000))
                .andExpect(jsonPath("$.data.sku").value("SOFA-NVY"))
                .andExpect(jsonPath("$.data.stock").value(8))
                .andExpect(jsonPath("$.data.display_order").value(3))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("옵션 삭제가 잘 되는가?")
    void deleteProductOption() throws Exception {
        mockMvc.perform(delete("/api/products/1/options/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 삭제되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("이미지 추가가 잘 되는가?")
    void addProductImage() throws Exception {
        mockMvc.perform(post("/api/products/8/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"url\": \"https://example.com/images/sofa3.jpg\",\n" +
                                " \"alt_text\": \"네이비 소파 측면\",\n" +
                                " \"is_primary\": false,\n" +
                                " \"display_order\": 3,\n" +
                                " \"option_id\": 35\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.url").value("https://example.com/images/sofa3.jpg"))
                .andExpect(jsonPath("$.data.alt_text").value("네이비 소파 측면"))
                .andExpect(jsonPath("$.data.is_primary").value(false))
                .andExpect(jsonPath("$.data.display_order").value(3))
                .andExpect(jsonPath("$.data.option_id").value(35))
                .andExpect(jsonPath("$.message").value("상품 이미지가 성공적으로 추가되었습니다."))
                .andDo(print());
    }
}
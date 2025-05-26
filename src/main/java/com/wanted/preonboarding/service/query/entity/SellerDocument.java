package com.wanted.preonboarding.service.query.entity;

import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "sellers")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SellerDocument {

    @Id
    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private BigDecimal rating;
    private String contactEmail;
    private String contactPhone;
    private LocalDateTime createdAt;
}

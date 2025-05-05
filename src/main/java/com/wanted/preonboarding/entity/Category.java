package com.wanted.preonboarding.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    private String name;
    private String slug;
    private String description;
    private Integer level;
    private String imageUrl;

    @Builder
    public Category(Category parent, String name, String slug, String description, Integer level, String imageUrl) {
        this.parent = parent;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.level = level;
        this.imageUrl = imageUrl;
    }
}

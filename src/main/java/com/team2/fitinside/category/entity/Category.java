package com.team2.fitinside.category.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    private Long displayOrder;

    // Soft delete를 위한 필드
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private CategoryImage categoryImage;

    // 삭제 시 isDeleted를 true로 설정하는 메서드
    public void delete() {
        this.isDeleted = true;
    }

    public void updateCategory(String name, Long displayOrder, Category parent) {
        this.name = name;
        this.displayOrder = displayOrder;
        this.parent = parent;
    }

    // 필요한 필드로 Category 생성하는 정적 메서드
//    public static Category create(String name, Long displayOrder, Category parent) {
//        return new Category(null, name, displayOrder, false, parent, new ArrayList<>(), null);
//    }
}


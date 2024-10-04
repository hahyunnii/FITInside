package com.team2.fitinside.category.entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CategoryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryImage_id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY) // OneToOne이 맞나?
    @JoinColumn(name = "category_id", nullable = false, unique = true)
    private Category category;

    // 이미지 순서
    @Column(name = "display_order")
    private Long displayOrder;

    // 소프트 삭제용 필드
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // 소프트 삭제 메서드
    public void delete() {
        this.isDeleted = true;
    }

    // 필요한 필드로 CategoryImage 생성하는 정적 메서드
    public static CategoryImage create(String imageUrl, Long displayOrder, Category category) {
        return new CategoryImage(null, imageUrl, category, displayOrder, false);
    }
}



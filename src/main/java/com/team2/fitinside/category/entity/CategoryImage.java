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
}


